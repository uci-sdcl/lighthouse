package edu.uci.lighthouse.ui.views.actions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.CGraphNode;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.layouts.InvalidLayoutConfiguration;
import org.eclipse.zest.layouts.algorithms.AbstractLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;

import edu.uci.lighthouse.model.LighthouseClass;

public class PackageLayoutAlgorithm extends AbstractLayoutAlgorithm {

	private int totalSteps;
	private int currentStep;
	
	Map<String, List<InternalNode>> mapPackageNameToEntities = new HashMap<String, List<InternalNode>>();
	
	public PackageLayoutAlgorithm(int styles) {
		super(styles);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void applyLayoutInternal(InternalNode[] entitiesToLayout,
			InternalRelationship[] relationshipsToConsider, double boundsX,
			double boundsY, double boundsWidth, double boundsHeight) {

//		totalSteps = entitiesToLayout.length;
//		double distance = boundsWidth / totalSteps;
//		int xLocation = 0;
//	
//		fireProgressStarted(totalSteps);
//		
//		for (currentStep = 0; currentStep < entitiesToLayout.length; currentStep++) {
//			LayoutEntity layoutEntity = entitiesToLayout[currentStep].getLayoutEntity();
//			layoutEntity.setLocationInLayout(xLocation, layoutEntity.getYInLayout());
//			xLocation+= distance;
//			fireProgressEvent(currentStep, totalSteps);
//		}
//		fireProgressEnded(totalSteps);
	for (InternalNode entity : entitiesToLayout) {
		Object  graphData = entity.getLayoutEntity().getGraphData();
		if (graphData != null) {
			CGraphNode node = (CGraphNode) graphData;
			Object nodeData = entity.getGraphData();
			if (nodeData instanceof  LighthouseClass){
				LighthouseClass aClass = (LighthouseClass) nodeData;
				List<InternalNode> nodes = mapPackageNameToEntities.get(aClass.getPackageName());
				if (nodes == null){
					mapPackageNameToEntities.put(aClass.getPackageName(),new LinkedList<InternalNode>());
				}
				nodes.add(entity);
			}
		}
	}
	
	GridLayoutAlgorithm grid1 = new GridLayoutAlgorithm(getStyle());
	
	
	GridLayoutAlgorithm grid2 = new GridLayoutAlgorithm(getStyle());
	
	
	
	
//	for (int i = 0; i < algorithms.length; i++) {
//		try {
//			algorithms[i].applyLayout(entitiesToLayout, relationshipsToConsider, boundsX, boundsY, boundsWidth, boundsHeight, this.internalAsynchronous, this.internalContinuous);
//		} catch (InvalidLayoutConfiguration e) {
//			e.printStackTrace();
//		}
//	}
//	for (int i = 0; i < entitiesToLayout.length; i++) {
//		entitiesToLayout[i].getLayoutEntity().setLocationInLayout(entitiesToLayout[i].getXInLayout(), entitiesToLayout[i].getYInLayout());
//	}
	
	
	
	
//		n.getLayoutEntity().getGraphData()
		GraphViewer v;
		Graph g;
//		v.findGraphItem(element) 
		
	}

	@Override
	protected int getCurrentLayoutStep() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getTotalNumberOfLayoutSteps() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected boolean isValidConfiguration(boolean asynchronous,
			boolean continuous) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void postLayoutAlgorithm(InternalNode[] entitiesToLayout,
			InternalRelationship[] relationshipsToConsider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void preLayoutAlgorithm(InternalNode[] entitiesToLayout,
			InternalRelationship[] relationshipsToConsider, double x, double y,
			double width, double height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLayoutArea(double x, double y, double width, double height) {
		// TODO Auto-generated method stub
		
	}

}
