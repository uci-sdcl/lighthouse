/*******************************************************************************
* Copyright (c) {2009,2011} {Software Design and Collaboration Laboratory (SDCL)
*				, University of California, Irvine}.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    {Software Design and Collaboration Laboratory (SDCL)
*	, University of California, Irvine}
*			- initial API and implementation and/or initial documentation
*******************************************************************************/
package edu.uci.lighthouse.ui.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.draw2d.Animation;
import org.eclipse.draw2d.IFigure;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import edu.uci.lighthouse.model.ILighthouseModelListener;
import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseField;
import edu.uci.lighthouse.model.LighthouseMethod;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.LighthouseRelationship;
import edu.uci.lighthouse.model.LighthouseEvent.TYPE;
import edu.uci.lighthouse.ui.graph.IUmlClass;
import edu.uci.lighthouse.ui.graph.UmlClassFigure;
import edu.uci.lighthouse.ui.graph.UmlClassNode;
import edu.uci.lighthouse.ui.swt.util.ColorFactory;
import edu.uci.lighthouse.ui.views.actions.LayoutDropDownAction;
import edu.uci.lighthouse.ui.views.actions.LinkWithEditorAction;

public class EmergingDesignView extends ThumbnailView implements ILighthouseModelListener, SelectionListener, ISelectionListener{

	private List<IAction> modes;
	private List<IAction> layouts;
	private EditorListener editorListener = new EditorListener();
	
	private static Logger logger = Logger.getLogger(EmergingDesignView.class);
	//LayoutDropDownAction action;
	
	/** */
	private Graph graph;
	//int level = 0;
	
	/* (non-Javadoc)
	 * @see edu.uci.lighthouse.ui.views.ThumbnailView#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		//initializeDummyModel();
		
		// Create the graph and populate it
		graph = new Graph(parent, SWT.None);
		//graph.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), false);
		//graph.setLayoutAlgorithm(new RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), false);
		populate(graph);
		graph.addSelectionListener(this);		
		/*
		graph.addMouseListener(new MouseAdapter(){
			public void mouseDown(MouseEvent e) {
				level = ++level % 4;
				refreshFigures(graph);
			}
		});
		*/
		
		graph.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				IFigure fig = graph.getFigureAt(e.x, e.y);
				if (fig instanceof UmlClassFigure){
					UmlClassFigure classFig = (UmlClassFigure)fig;
					LighthouseEntity entity = classFig.findLighthouseEntityAt(e.x, e.y);
					if (entity != null){
						showInEditor(entity);
					}
				}
			}
		});
		
		
		//graph.applyLayout();
		
		
		// Set the parameters for the thumbnail view
		setViewport(graph.getViewport());
		setSource(graph.getContents());
		setMainComposite(graph);
		
		//
		createActions();
		hookPullDownMenu();
		LinkWithEditorAction link = new LinkWithEditorAction(null);
		getViewSite().getActionBars().getToolBarManager().add(link);
		//action = new LayoutDropDownAction(graph);
		getViewSite().getActionBars().getToolBarManager().add(new LayoutDropDownAction(graph));
		//getViewSite().getActionBars().getToolBarManager().add(new DiagramModeDropDownAction(graph));
		
		
		editorListener.addEditorSelectionListener(link);
		
		modes.get(0).run();
		layouts.get(3).run();
		
		LighthouseModel model = LighthouseModel.getInstance();
		model.addModelListener(this);
		//IPartService
		//getSite().getPage().addSelectionListener(this);
		getSite().getPage().addPartListener(editorListener);
		
		//selectionChanged(getSite().getPage().getActivePart(),null);
		logger.info("view started!");
	}

	@Override
	public void dispose() {
		logger.debug("dispose");
		//getSite().getPage().removeSelectionListener(this);
		getSite().getPage().removePartListener(editorListener);
		super.dispose();
	}

	private void populate(Graph g) {
		LighthouseModel model = LighthouseModel.getInstance();
		
		HashMap<LighthouseClass,UmlClassNode> cacheNodes = new HashMap<LighthouseClass,UmlClassNode>();		
		for (LighthouseClass c : model.getAllClasses()) {			
			UmlClassNode node = new UmlClassNode(g, SWT.NONE, c);	
			populateNode(node);
			cacheNodes.put(c, node);
		}

		HashMap<String,LighthouseRelationship> cacheConnections = new HashMap<String,LighthouseRelationship>();
		LighthouseModelManager manager = new LighthouseModelManager(model);
		for (LighthouseRelationship r : model.getRelationships()) {
			LighthouseClass fromClass = manager.getMyClass(r.getFromEntity());
			LighthouseClass toClass = manager.getMyClass(r.getToEntity());			
			if (fromClass != null && toClass != null && !fromClass.equals(toClass)){
				String keyFrom = fromClass.getFullyQualifiedName()+"-"+toClass.getFullyQualifiedName();
				String keyTo = toClass.getFullyQualifiedName()+"-"+fromClass.getFullyQualifiedName();
				if (cacheConnections.get(keyFrom)== null&&cacheConnections.get(keyTo)== null){
					UmlClassNode fromNode = cacheNodes.get(fromClass);
					UmlClassNode toNode = cacheNodes.get(toClass);
					if (fromNode!= null && toNode != null){
						//System.out.println(keyFrom);
						GraphConnection c = new GraphConnection(graph,/*SWT.NONE*/ZestStyles.CONNECTIONS_SOLID , fromNode, toNode);
						//c.setHighlightColor(ColorFactory.classBorder);
						//c.setLineColor(ColorConstants.black);	
						cacheConnections.put(keyFrom, r);
						cacheConnections.put(keyTo, r);
					}
				}
			}
		}		
	}
	
	private void populateNode(UmlClassNode node){
		node.clear();
		LighthouseModel model = LighthouseModel.getInstance();
		Collection<LighthouseEntity> ma = model.getMethodsAndAttributesFromClass(node.getLighthouseClass());
		for (LighthouseEntity e : ma) {
			if (e instanceof LighthouseField) {
				node.addField((LighthouseField)e);
			} else if (e instanceof LighthouseMethod){
				node.addMethod((LighthouseMethod)e);
			}
		}
	}
	
	private void refreshFigures(Graph g, int level){
		Animation.markBegin();
		for (Iterator itNodes = g.getNodes().iterator(); itNodes.hasNext();) {
			GraphNode node = (GraphNode) itNodes.next();
			if (node instanceof UmlClassNode){	
				UmlClassNode classNode = (UmlClassNode) node;
				classNode.setLevel(IUmlClass.LEVEL.values()[level]);
			}
		}
		Animation.run(150);
	}

	@Override
	public void classChanged(LighthouseClass arg0, TYPE type) {
		for (Iterator itNodes = graph.getNodes().iterator(); itNodes.hasNext();) {
			GraphNode node = (GraphNode) itNodes.next();
			if (node instanceof UmlClassNode) {
				final UmlClassNode classNode = (UmlClassNode) node;
				if (classNode.getLighthouseClass().equals(arg0)) {
					populateNode(classNode);
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							Animation.markBegin();
							classNode.rebuild();
							Animation.run(150);
						}
					});
				}
			}
		}
	}

	@Override
	public void modelChanged() {
		// Create the graph and populate it
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				logger.info("model changed!");
				populate(graph);
			}
		});
		graph.applyLayout();
	}
	
	private void createActions() {
		modes = new ArrayList<IAction>();
		for (int i = 0; i < 4; i++) {
			final int mode = i;
			modes.add(new Action("Mode "+String.valueOf(i+1),IAction.AS_CHECK_BOX){
				@Override
				public void run() {
					uncheckActions(modes);					
					refreshFigures(graph, mode);
					setChecked(true);
				}			
			});							
		}
		layouts = new ArrayList<IAction>();
		layouts.add(new Action("Spring",IAction.AS_CHECK_BOX){
			@Override
			public void run() {
				uncheckActions(layouts);	
				graph.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
				setChecked(true);
			}
		});
		layouts.add(new Action("Tree",IAction.AS_CHECK_BOX){
			@Override
			public void run() {
				uncheckActions(layouts);
				graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
				setChecked(true);
			}
		});
		layouts.add(new Action("Radial",IAction.AS_CHECK_BOX){
			@Override
			public void run() {
				uncheckActions(layouts);
				graph.setLayoutAlgorithm(new RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
				setChecked(true);
			}
		});
		layouts.add(new Action("Grid",IAction.AS_CHECK_BOX){
			@Override
			public void run() {
				uncheckActions(layouts);
				graph.setLayoutAlgorithm(new GridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
				setChecked(true);
			}
		});	
	}
	
	private void uncheckActions(List<IAction> list){
		for (IAction action : list) {
			action.setChecked(false);
		}
	}
	
	private void hookPullDownMenu() {
		IMenuManager manager = getViewSite().getActionBars().getMenuManager();
		for (int i = 0; i < 4; i++) {
			manager.add(modes.get(i));
						
		}
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		for (int i = 0; i < 4; i++) {
			manager.add(layouts.get(i));						
		}	
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		unhighlight(graph.getConnections());
		if (e.item instanceof GraphNode){
			GraphNode node = (GraphNode) e.item;
			highlight(node.getSourceConnections());
			highlight(node.getTargetConnections());
			//
		}
	}
	
	private void unhighlight(List connections){
		for (Iterator itConn = connections.iterator(); itConn.hasNext();) {
			GraphConnection conn = (GraphConnection) itConn.next();
			conn.unhighlight();
		}
	}
	
	private void highlight(List connections){
		for (Iterator itConn = connections.iterator(); itConn.hasNext();) {
			GraphConnection conn = (GraphConnection) itConn.next();
			conn.highlight();
		}
	}
	
	private void showInEditor(LighthouseEntity e) {
		LighthouseModelManager manager = new LighthouseModelManager(LighthouseModel.getInstance());
		LighthouseClass c = manager.getMyClass(e);
		if (c != null) {
			try {
				IWorkspace workspace = ResourcesPlugin.getWorkspace();
				IJavaModel javaModel = JavaCore.create(workspace.getRoot());
				IJavaProject[] projects = javaModel.getJavaProjects();
				for (int i = 0; i < projects.length; i++) {
					IType type = projects[i].findType(c.getFullyQualifiedName());
					if (type != null && type instanceof IJavaElement) {
						IJavaElement element = (IJavaElement) type;

						IJavaElement[] elements;
						if (e instanceof LighthouseMethod) {
							elements = type.getMethods();
						} else if (e instanceof LighthouseField) {
							elements = type.getFields();
						} else {
							elements = new IJavaElement[0];
						}
						for (int j = 0; j < elements.length; j++) {
							if (elements[j].toString().replaceAll(" ", "")
									.indexOf(e.getShortName()) != -1) {
								element = elements[j];
								break;
							}
						}
						IEditorPart javaEditor = JavaUI
								.openInEditor(/* cu */element);
						break;
					}
				}
			} catch (JavaModelException ex) {
				ex.printStackTrace();
			} catch (PartInitException ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part != null) {
			String shortName = part.getTitle().replaceAll(".java", "");
			for (Iterator itNodes = graph.getNodes().iterator(); itNodes
					.hasNext();) {
				GraphNode node = (GraphNode) itNodes.next();
				if (shortName.equals(node.getText())) {
					//node.highlight();
					node.getNodeFigure().setBackgroundColor(ColorFactory.aliceBlue);
				} else {
					//node.unhighlight();
					node.setBackgroundColor(ColorFactory.white);
				}
			}
		}
	}

	@Override
	public void relationshipChanged(LighthouseRelationship relationship,
			TYPE type) {
		// TODO Auto-generated method stub
		
	}

}
