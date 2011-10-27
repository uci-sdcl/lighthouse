/*
* Sourcerer: an infrastructure for large-scale source code analysis.
* Copyright (C) by contributors. See CONTRIBUTORS.txt for full list.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
package edu.uci.ics.sourcerer.extractor.ast;

import static edu.uci.ics.sourcerer.util.io.Logging.logger;

import java.util.Deque;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberRef;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.MethodRefParameter;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.WildcardType;
import org.eclipse.jdt.internal.core.SourceType;

import edu.uci.ics.sourcerer.extractor.io.ICommentWriter;
import edu.uci.ics.sourcerer.extractor.io.IEntityWriter;
import edu.uci.ics.sourcerer.extractor.io.IFileWriter;
import edu.uci.ics.sourcerer.extractor.io.IImportWriter;
import edu.uci.ics.sourcerer.extractor.io.IRelationWriter;
import edu.uci.ics.sourcerer.extractor.io.WriterBundle;
import edu.uci.ics.sourcerer.model.Entity;
import edu.uci.ics.sourcerer.util.Helper;

public class ReferenceExtractorVisitor extends ASTVisitor {
  private IImportWriter importWriter;
  private IEntityWriter entityWriter;
  private IRelationWriter relationWriter;
  private ICommentWriter commentWriter;
  private IFileWriter fileWriter;

  private String compilationUnitPath = null;

  private FQNStack fqnStack = new FQNStack();

  public ReferenceExtractorVisitor(WriterBundle writers) {
    importWriter = writers.getImportWriter();
    entityWriter = writers.getEntityWriter();
    relationWriter = writers.getRelationWriter();
    commentWriter = writers.getCommentWriter();
    fileWriter = writers.getFileWriter();
  }

  @Override
  public boolean visit(ImportDeclaration node) {
    try {
      IBinding binding = node.resolveBinding();
      if (binding == null) {
        importWriter.writeImport(compilationUnitPath, node.getName().getFullyQualifiedName(), node.isStatic(), node.isOnDemand());
      } else {
        if (binding instanceof ITypeBinding) {
          importWriter.writeImport(compilationUnitPath, getTypeFqn((ITypeBinding)binding), node.isStatic(), node.isOnDemand());
        } else {
          importWriter.writeImport(compilationUnitPath, node.getName().getFullyQualifiedName(), node.isStatic(), node.isOnDemand());
        }
      }
    } catch (Exception e) {
      logger.log(Level.FINE, "Eclipse NPE bug in import");
      importWriter.writeImport(compilationUnitPath, node.getName().getFullyQualifiedName(), node.isStatic(), node.isOnDemand());
    }
    return false;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean visit(CompilationUnit node) {
    compilationUnitPath = node.getJavaElement().getResource().getRawLocation().toString();
    int errorCount = 0;
    for (IProblem problem : node.getProblems()) {
      if (problem.isError()) {
        errorCount++;
      }
    }
    fileWriter.writeFile(compilationUnitPath, errorCount);
    StringBuilder fqn = new StringBuilder(getProjectName(node.getJavaElement()));
    if (node.getPackage() != null) {
      fqn.append("."+node.getPackage().getName().getFullyQualifiedName());
    }
    fqnStack.push(fqn.toString(), Entity.PACKAGE);
    for (ASTNode comment : (List<ASTNode>)node.getCommentList()) {
      if (comment.getParent() == null) {
        comment.accept(this);
      }
    }
    return true;
  }
  
  private String getProjectName(IJavaElement element){
	 String result = "";
	 if (element != null && element.getJavaProject() != null){
		 result = element.getJavaProject().getElementName();
	 }
	 return result;
  }

  @Override
  public void endVisit(CompilationUnit node) {
    compilationUnitPath = null;
    if (node.getPackage() != null) {
      fqnStack.pop();
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean visit(TypeDeclaration node) {
    // Get the fqn
    String fqn = null;
    Entity type = null;
    if (node.isPackageMemberTypeDeclaration()) {
      fqn = fqnStack.getTypeFqn(node.getName().getIdentifier());
    } else if (node.isMemberTypeDeclaration()) {
      if (node.getName().getIdentifier().length() > 0) {
        fqn = fqnStack.getFqn() + "$" + node.getName().getIdentifier();
      } else {
        logger.log(Level.SEVERE, "A type declaration should not declare an annonymous type!");
        fqn = fqnStack.getAnonymousClassFqn();
      }
    } else if (node.isLocalTypeDeclaration()) {
      ITypeBinding binding = node.resolveBinding();
      if (binding == null) {
        fqn = fqnStack.getAnonymousClassFqn() + node.getName();
      } else {
        fqn = getTypeFqn(binding);
      }
    } else {
      logger.log(Level.SEVERE, "Unsure what type the declaration is!");
      fqn = "_ERROR_";
    }
    
    // Write the entity
    if (node.isInterface()) {
      entityWriter.writeInterface(fqn, node.getModifiers(), compilationUnitPath, node.getStartPosition(), node.getLength());
      type = Entity.INTERFACE;
    } else {
      entityWriter.writeClass(fqn, node.getModifiers(), compilationUnitPath, node.getStartPosition(), node.getLength());
      type = Entity.CLASS;
    }
    
    // Write the inside relation
    relationWriter.writeInside(fqn, fqnStack.getFqn());
    
    // Write the extends relation
    Type superType = node.getSuperclassType();
    if (superType != null) {
      String superFqn = getTypeFqn(superType);
      relationWriter.writeExtends(fqn, superFqn);
    }
    
    // Write the implements relation
    List<Type> superInterfaceTypes = node.superInterfaceTypes();
    for (Type superInterfaceType : superInterfaceTypes) {
      String superFqn = getTypeFqn(superInterfaceType);
      relationWriter.writeImplements(fqn, superFqn);
    }
    
    // Write the parametrized by relation
    int pos = 0;
    for (TypeParameter typeParam : (List<TypeParameter>)node.typeParameters()) {
      relationWriter.writeParametrizedBy(fqn, getTypeParam(typeParam), pos++);
    }
    
    ITypeBinding binding = node.resolveBinding();
    if (binding != null) {
      if (binding.isAnonymous()) {
        logger.log(Level.SEVERE, "A type declaration should not declare an annonymous type!");
      } else {
        // Verify the fqn
        String fqn2 = getTypeFqn(binding);
        if (!fqn.equals(fqn2)) {
          logger.log(Level.SEVERE, "Mismatch between " + fqn + " and " + fqn2);
        }

        // Write out the synthesized constructors
        for (IMethodBinding method : binding.getDeclaredMethods()) {
          if (method.isDefaultConstructor()) {
            // Write the entity
            String constructorFqn = getMethodFqn(method, false);
            entityWriter.writeConstructor(constructorFqn, method.getModifiers(), compilationUnitPath, -1, 0);

            // Write the inside relation
            relationWriter.writeInside(constructorFqn, fqn);
          }
        }
      }
    }
      
    fqnStack.push(fqn, type);

    return true;
  }

  @Override
  public void endVisit(TypeDeclaration node) {
    fqnStack.pop();
  }

  @Override
  public boolean visit(AnonymousClassDeclaration node) {
    // Get the fqn
    String fqn = fqnStack.getAnonymousClassFqn();
    
    // Write the entity
    entityWriter.writeClass(fqn, 0, compilationUnitPath, node.getStartPosition(), node.getLength());
    
    // Write the inside relation
    relationWriter.writeInside(fqn, fqnStack.getFqn());
    
    if (node.getParent() instanceof ClassInstanceCreation) {
      ClassInstanceCreation parent = (ClassInstanceCreation) node.getParent();
      Type superType = parent.getType();
      ITypeBinding superBinding = superType.resolveBinding();
      
      if (superBinding == null) {
        // Write the uses relation
        // Can't do extends/implements because unsure if it's an interface or class type
        relationWriter.writeUses(fqn, getTypeFqn(superType));
      } else {
        if (superBinding.isInterface()) {
          // Write the implements relation
          relationWriter.writeImplements(fqn, getTypeFqn(superBinding));
        } else {
          // Write the extends relation
          relationWriter.writeExtends(fqn, getTypeFqn(superBinding));
        }
      }
    } else {
      // It's extending an enum
      relationWriter.writeExtends(fqn, fqnStack.getFqn());
    }
    
    ITypeBinding binding = node.resolveBinding();
    
    if (binding != null) {
      // Write out the synthesized constructors
      for (IMethodBinding method : binding.getDeclaredMethods()) {
        if (method.isConstructor()) {
          // Write the entity
          String constructorFqn = getAnonymousConstructorFqn(fqn, method);
          entityWriter.writeConstructor(constructorFqn, method.getModifiers(), compilationUnitPath, -1, 0);

          // Write the inside relation
          relationWriter.writeInside(constructorFqn, fqn);

          // Write the calls relation
          relationWriter.writeCalls(fqnStack.getFqn(), constructorFqn);

          // Write the receives relations
          int count = 0;
          for (ITypeBinding param : method.getParameterTypes()) {
            relationWriter.writeReceives(constructorFqn, getTypeFqn(param), "_ANONYMOUS_", count++);
          }
          
          // Reference the superconstructor
          ITypeBinding superClassBinding = binding.getSuperclass();
          if (superClassBinding != null) {
            superClassBinding = superClassBinding.getErasure();
          }
          if (superClassBinding != null) {
            String superFqn = getTypeFqn(superClassBinding);
            String superConstructorFqn = getAnonymousConstructorFqn(superFqn, method);
            relationWriter.writeCalls(constructorFqn, superConstructorFqn);
          }
        }
      }
    }
    
    fqnStack.push(fqn, Entity.CLASS);
    return true;
  }

  @Override
  public void endVisit(AnonymousClassDeclaration node) {
    fqnStack.pop();
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean visit(EnumDeclaration node) {
    // Get the fqn
    String fqn = null;
    if (node.isPackageMemberTypeDeclaration()) {
      fqn = fqnStack.getTypeFqn(node.getName().getIdentifier());
    } else if (node.isMemberTypeDeclaration()) {
      fqn = fqnStack.getFqn() + "$" + node.getName().getIdentifier();
    } else if (node.isLocalTypeDeclaration()) {
      logger.log(Level.SEVERE, "Can't have local enums!");
    } else {
      logger.log(Level.SEVERE, "Unsure what type the declaration is!");
      fqn = "_ERROR_";
    }

    // Write the entity
    entityWriter.writeEnum(fqn, node.getModifiers(), compilationUnitPath, node.getStartPosition(), node.getLength());
    
    // Write the inside relation
    relationWriter.writeInside(fqn, fqnStack.getFqn());
    
    // Write the implements relation
    for (Type superInterfaceType : (List<Type>) node.superInterfaceTypes()) {
      relationWriter.writeImplements(fqn, getTypeFqn(superInterfaceType));
    }
    
    ITypeBinding binding = node.resolveBinding();
    if (binding != null) {
      // Write out the synthesized constructors
      for (IMethodBinding method : binding.getDeclaredMethods()) {
        if (method.isDefaultConstructor()) {
          // Write the entity
          String constructorFqn = getMethodFqn(method, false);
          entityWriter.writeConstructor(constructorFqn, method.getModifiers(), compilationUnitPath, -1, 0);

          // Write the inside relation
          relationWriter.writeInside(constructorFqn, fqn);
        }
      }
    }
    
    fqnStack.push(fqn, Entity.ENUM);
    
    return true;
  }

  @Override
  public void endVisit(EnumDeclaration node) {
    fqnStack.pop();
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean visit(EnumConstantDeclaration node) {
    // Get the fqn
    String fqn = fqnStack.getTypeFqn(node.getName().getIdentifier());
    
    // Write the entity
    entityWriter.writeEnumConstant(fqn, 0, compilationUnitPath, node.getStartPosition(), node.getLength());
    
    // Write the inside relation
    relationWriter.writeInside(fqn, fqnStack.getFqn());
    
    // Write the holds relation
    relationWriter.writeHolds(fqn, fqnStack.getFqn());
    
    // Write the calls relation
    IMethodBinding methodBinding = node.resolveConstructorBinding();
    if (methodBinding == null) {
      String methodFqn = fqnStack.getFqn() + ".<init>" + getFuzzyMethodArgs(node.arguments());
      relationWriter.writeCalls(fqn, methodFqn);
    } else {
      // Write the calls relation
      relationWriter.writeCalls(fqn, getMethodFqn(methodBinding, false));
    }
    
    // Fush the enum constant onto the stack
    fqnStack.push(fqn, Entity.ENUM_CONSTANT);
    return true;
  }
  
  public void endVisit(EnumConstantDeclaration node) {
    fqnStack.pop();
  }

  @Override
  public boolean visit(Initializer node) {
    // Get the fqn
    String fqn = fqnStack.getInitializerFqn();

    // Write the entity
    entityWriter.writeInitializer(fqn, node.getModifiers(), compilationUnitPath, node.getStartPosition(), node.getLength());

    // Write the inside relation
    relationWriter.writeInside(fqn, fqnStack.getFqn());

    fqnStack.push(fqn, Entity.INITIALIZER);

    return true;
  }

  @Override
  public void endVisit(Initializer node) {
    fqnStack.pop();
  }

  @Override
  public boolean visit(FieldDeclaration node) {
    return true;
  }

  @Override
  public boolean visit(SingleVariableDeclaration node) {
    IVariableBinding binding = node.resolveBinding();

    if (binding == null) {
      // Write the uses relation
      relationWriter.writeUses(fqnStack.getFqn(), getTypeFqn(node.getType()));
    } else {
      String typeFqn = getTypeFqn(binding.getType());
      if (binding.isParameter()) {
        // Write the receives relation
        relationWriter.writeReceives(fqnStack.getFqn(), typeFqn, binding.getName(), fqnStack.getNextParameterPos());
      }
      // Write the uses relation
      relationWriter.writeUses(fqnStack.getFqn(), typeFqn);
    }
    return true;
  }

  @Override
  public boolean visit(VariableDeclarationFragment node) {
    if (node.getParent() instanceof FieldDeclaration){
      FieldDeclaration parent = (FieldDeclaration)node.getParent();
      
      // Get the fqn
      String fqn = fqnStack.getTypeFqn(node.getName().getIdentifier());
      
      // Write the entity
      entityWriter.writeField(fqn, parent.getModifiers(), compilationUnitPath, node.getStartPosition(), node.getLength());
      
      // Write the inside relation
      relationWriter.writeInside(fqn, fqnStack.getFqn());
      
      // Write the holds relation
      relationWriter.writeHolds(fqn, getTypeFqn(parent.getType()));
      
//      // Write the uses relation
//      relationWriter.writeUses(fqnStack.getFqn(), getTypeFqn(parent.getType()));
      
      // Add the field to the fqnstack
      fqnStack.push(fqn, Entity.FIELD);
    } else if (node.getParent() instanceof VariableDeclarationStatement) {
      VariableDeclarationStatement parent = (VariableDeclarationStatement)node.getParent();
      // Write the uses relation
      relationWriter.writeUses(fqnStack.getFqn(), getTypeFqn(parent.getType()));
    } else if (node.getParent() instanceof VariableDeclarationExpression) {
      VariableDeclarationExpression parent = (VariableDeclarationExpression)node.getParent();
      // Write the uses relation
      relationWriter.writeUses(fqnStack.getFqn(), getTypeFqn(parent.getType()));
    } else if (node.getParent() instanceof ForStatement) {
      logger.log(Level.SEVERE, "So it is true...");
    } else {
      logger.log(Level.SEVERE, "Unknown parent for variable declaration fragment.");
    }
    
    IVariableBinding binding = node.resolveBinding();
    if (binding != null && binding.isField() && !(node.getParent() instanceof FieldDeclaration)) {
      logger.log(Level.SEVERE, "It's a field but it shouldn't be!");
    }

    return true;
  }
  
  @Override
  public void endVisit(VariableDeclarationFragment node) {
    if (node.getParent() instanceof FieldDeclaration) {
      fqnStack.pop();
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean visit(MethodDeclaration node) {
    // Build the fqn
    String fqn = null;
    if (node.isConstructor()) {
      fqn = getFuzzyConstructorFqn(node);
      
      // Write the entity
      entityWriter.writeConstructor(fqn, node.getModifiers(), compilationUnitPath, node.getStartPosition(), node.getLength());
    } else {
      fqn = getFuzzyMethodFqn(node);
      
      // Write the entity
      entityWriter.writeMethod(fqn, node.getModifiers(), compilationUnitPath, node.getStartPosition(), node.getLength());
      
      // Write the returns relation
      relationWriter.writeReturns(fqn, getTypeFqn(node.getReturnType2()));
    }
    
    // Write the inside relation
    relationWriter.writeInside(fqn, fqnStack.getFqn());

    // Write the throws relation
    for (Name name : (List<Name>)node.thrownExceptions()) {
      ITypeBinding exceptionBinding = name.resolveTypeBinding();
      if (exceptionBinding == null) {
        relationWriter.writeThrows(fqn, "_UNRESOLVED_." + name.getFullyQualifiedName());
      } else {
        relationWriter.writeThrows(fqn, getTypeFqn(exceptionBinding));
      }
    }
    
    // Write the receives relation
    int pos = 0;
    for (SingleVariableDeclaration param : (List<SingleVariableDeclaration>)node.parameters()) {
      relationWriter.writeReceives(fqn, getTypeFqn(param.getType()), param.getName().getFullyQualifiedName(), pos++);
    }
    
    // Write the parametrized by relation
    pos = 0;
    for (TypeParameter typeParam : (List<TypeParameter>)node.typeParameters()) {
      relationWriter.writeParametrizedBy(fqn, getTypeParam(typeParam), pos++);
    }
      
    fqnStack.push(fqn, Entity.METHOD);

    return true;
  }

  @Override
  public void endVisit(MethodDeclaration node) {
    fqnStack.pop();
  }

  @Override
  public boolean visit(MethodInvocation node) {
    // Get the fqn
    String fqn = null;
    IMethodBinding binding = node.resolveMethodBinding();
    if (binding == null) {
      fqn = getFuzzyMethodFqn(node);
    } else {
      fqn = getMethodFqn(binding, false);
    }
    
    // Write the calls relation
    relationWriter.writeCalls(fqnStack.getFqn(), fqn);
    
    return true;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean visit(SuperMethodInvocation node) {
    // Get the fqn
    String fqn = null;
    IMethodBinding binding = node.resolveMethodBinding();
    if (binding == null) {
      fqn = "_SUPER_." + node.getName().getIdentifier() + getFuzzyMethodArgs(node.arguments());
    } else {
      fqn = getMethodFqn(binding, false);
      
    } 
    
    // Write the calls relation
    relationWriter.writeCalls(fqnStack.getFqn(), fqn);

    return true;
  }

  @Override
  public boolean visit(ClassInstanceCreation node) {
    // Get the fqn
    String fqn = null;
    IMethodBinding binding = node.resolveConstructorBinding();
    if (binding == null) {
      fqn = getFuzzyConstructorFqn(node);
    } else {
      fqn = getMethodFqn(binding, false);
    }

    // Write the call relation
    relationWriter.writeCalls(fqnStack.getFqn(), fqn);

    return true;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean visit(ConstructorInvocation node) {
    // Get the fqn
    String fqn = null;
    IMethodBinding binding = node.resolveConstructorBinding();
    if (binding == null) {
      fqn = fqnStack.getFqn() + ".<init>" + getFuzzyMethodArgs(node.arguments()); 
    } else {
      fqn = getMethodFqn(binding, false);
    }

    // Write the calls relation
    relationWriter.writeCalls(fqnStack.getFqn(), fqn);

    return true;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean visit(SuperConstructorInvocation node) {
    // Get the fqn
    String fqn = null;
    IMethodBinding binding = node.resolveConstructorBinding();
    if (binding == null) {
      fqn = "_SUPER_" + getFuzzyMethodArgs(node.arguments());
    } else {
      fqn = getMethodFqn(binding, false);
    }

    // Write the call relation
    relationWriter.writeCalls(fqnStack.getFqn(), fqn);

    return true;
  }

  @Override
  public boolean visit(CastExpression node) {
    // Write the uses relation
    relationWriter.writeUses(fqnStack.getFqn(), getTypeFqn(node.getType()));

    return true;
  }

  @Override
  public boolean visit(ThrowStatement node) {
    ITypeBinding binding = node.getExpression().resolveTypeBinding();
    if (binding != null) {
      // Write the throws relation
      relationWriter.writeThrows(fqnStack.getFqn(), getTypeFqn(binding));
    }

    return true;
  }

  @Override
  public boolean visit(CatchClause node) {
    // Write the uses relation
    relationWriter.writeUses(fqnStack.getFqn(), getTypeFqn(node.getException().getType()));
    
    return true;
  }

  @Override
  public boolean visit(FieldAccess node) {
    // Get the fqn
    String fqn = null;
    IVariableBinding binding = node.resolveFieldBinding();
    if (binding == null) {
      fqn = "_UNRESOLVED_." + node.getName().getIdentifier();
    } else {
      ITypeBinding declaringClass = binding.getDeclaringClass();
      if (declaringClass != null) {
        declaringClass = declaringClass.getErasure();
      }
      if (declaringClass == null) {
        if (binding.isRecovered()) {
          fqn = "_UNRESOLVED_." + binding.getName();
        } else if (node.getExpression().resolveTypeBinding().isArray() && binding.getName().equals("length")) {
          // Ignore array length
          return true;
        } else {
          logger.log(Level.SEVERE, "Non-recovered field binding without a declaring class that's not an array.length!");
        }
      } else {
        fqn = getTypeFqn(declaringClass) + "." + binding.getName();
      }
    }

    // Write the accesses relation
    relationWriter.writeAccesses(fqnStack.getFqn(), fqn);
    
    return true;
  }

  @Override
  public boolean visit(SuperFieldAccess node) {
    // Get the fqn
    String fqn = null;
    IVariableBinding binding = node.resolveFieldBinding();
    if (binding == null) {
      fqn = "_SUPER_." + node.getName().getIdentifier();
    } else {
      ITypeBinding declaringClass = binding.getDeclaringClass();
      if (declaringClass != null) {
        declaringClass = declaringClass.getErasure();
      }
      if (declaringClass == null) {
        fqn = "_SUPER_." + binding.getName();
      } else {
        fqn = getTypeFqn(declaringClass) + "." + binding.getName();
      }
    }
    
    // Write the accesses relation
    relationWriter.writeAccesses(fqnStack.getFqn(), fqn);

    return true;
  }

  @Override
  public boolean visit(QualifiedName node) {
    return super.visit(node);
  }

  @Override
  public boolean visit(SimpleName node) {
    IBinding binding = node.resolveBinding();

    if (binding instanceof IVariableBinding) {
      IVariableBinding varBinding = (IVariableBinding) binding;
      if (varBinding.isField()) {
        // Write the accesses relation
        ITypeBinding declaringClass = varBinding.getDeclaringClass();
        if (declaringClass != null) {
          declaringClass = declaringClass.getErasure();
        }
        if (declaringClass == null) {
          if (binding.isRecovered()) {
            // Write the access relation
            relationWriter.writeAccesses(fqnStack.getFqn(), "_UNRESOLVED_." + binding.getName());
          } else if (binding.getName().equals("length")) {
            return true;
          } else {
            logger.log(Level.SEVERE,
                "Non-recovered field binding without a declaring class that's not an array.length!", node);
          }
        } else {
          // Write the accesses relation
          relationWriter.writeAccesses(fqnStack.getFqn(), getTypeFqn(declaringClass) + "." + binding.getName());
        }
      }
    } else if (binding instanceof ITypeBinding) {
      ITypeBinding typeBinding = (ITypeBinding) binding;
      // Write the uses relation
      if (!fqnStack.isDeclaredTypeTop()) {
        typeBinding = typeBinding.getErasure();
        if (!typeBinding.isRecovered()) {
          relationWriter.writeUses(fqnStack.getFqn(), getTypeFqn(typeBinding));
        }
      }
    }
    return true;
  }

  @Override
  public boolean visit(ArrayCreation node) {
    // Write the uses relation
    relationWriter.writeUses(fqnStack.getFqn(), getTypeFqn(node.getType()));

    return true;
  }

  @Override
  public boolean visit(InstanceofExpression node) {
    // Write the uses relation
    relationWriter.writeUses(fqnStack.getFqn(), getTypeFqn(node.getRightOperand()));

    return true;
  }

  @Override
  public boolean visit(NullLiteral node) {
    return true;
  }

  @Override
  public boolean visit(BooleanLiteral node) {
    // Write the uses relation
    relationWriter.writeUses(fqnStack.getFqn(), "boolean");
    return true;
  }

  @Override
  public boolean visit(CharacterLiteral node) {
    // Write the uses relation
    relationWriter.writeUses(fqnStack.getFqn(), "char");
    return true;
  }

  @Override
  public boolean visit(StringLiteral node) {
    // Write the uses relation
    relationWriter.writeUses(fqnStack.getFqn(), "java.lang.String");
    return true;
  }

  @Override
  public boolean visit(NumberLiteral node) {
    // Write the uses relation
    relationWriter.writeUses(fqnStack.getFqn(), node.resolveTypeBinding().getQualifiedName());
    return true;
  }

  @Override
  public boolean visit(BlockComment node) {
    // Write the comment entity
    commentWriter.writeBlockComment(compilationUnitPath, node.getStartPosition(), node.getLength());
    return true;
  }

  @Override
  public boolean visit(LineComment node) {
    // Write the comment entity
    commentWriter.writeLineComment(compilationUnitPath, node.getStartPosition(), node.getLength());
    return true;
  }

  @Override
  public boolean visit(Javadoc node) {
    // Write the comment entity
    if (node.getParent() == null || node.getParent() instanceof PackageDeclaration) {
      commentWriter.writeUnassociatedJavadocComment(compilationUnitPath, node.getStartPosition(), node.getLength());
    } else {
      commentWriter.writeJavadocComment(fqnStack.getFqn(), node.getStartPosition(), node.getLength());
    }
    return true;
  }

  // Annotation Stuff
  @Override
  public boolean visit(AnnotationTypeDeclaration node) {
    // Get the fqn
    String fqn = fqnStack.getFqn() + "." + node.getName().getIdentifier();
    
    // Write the entity
    entityWriter.writeAnnotation(fqn, node.getModifiers(), compilationUnitPath, node.getStartPosition(), node.getLength());

    // Write the inside relation
    relationWriter.writeInside(fqn, fqnStack.getFqn());

    fqnStack.push(fqn, Entity.ANNOTATION);

    return true;
  }
  
  @Override
  public void endVisit(AnnotationTypeDeclaration node) {
    fqnStack.pop();
  }

  @Override
  public boolean visit(AnnotationTypeMemberDeclaration node) {
    // Get the fqn
    String fqn = fqnStack.getFqn() + "." + node.getName().getIdentifier() + "()";

    // Write the entity
    entityWriter.writeAnnotationMember(fqn, node.getModifiers(), compilationUnitPath, node.getStartPosition(), node.getLength());

    // Write the inside relation
    relationWriter.writeInside(fqn, fqnStack.getFqn());

    // Write the returns relation
    relationWriter.writeReturns(fqn, getTypeFqn(node.getType()));

    return true;
  }
  
  @Override
  public boolean visit(MarkerAnnotation node) {
    // Get the fqn
    String fqn = null;
    IAnnotationBinding binding = node.resolveAnnotationBinding();
    if (binding == null) {
      fqn = "_UNKNOWN_." + node.getTypeName();
    } else {
      ITypeBinding typeBinding = binding.getAnnotationType();
      if (typeBinding == null) {
        fqn = "_UNKNOWN_." + binding.getName();
      } else {
        fqn = getTypeFqn(typeBinding);
      }
    }    
    
    // Write the annotates relation
    relationWriter.writeAnnotates(fqnStack.getFqn(), fqn);
    
    return true;
  }
  
  @Override
  public boolean visit(NormalAnnotation node) {
    // Get the fqn
    String fqn = null;
    IAnnotationBinding binding = node.resolveAnnotationBinding();
    if (binding == null) {
      fqn = "_UNKNOWN_." + node.getTypeName();
    } else {
      ITypeBinding typeBinding = binding.getAnnotationType();
      if (typeBinding == null) {
        fqn = "_UNKNOWN_." + binding.getName();
      } else {
        fqn = getTypeFqn(typeBinding);
      }
    }    
    
    // Write the annotates relation
    relationWriter.writeAnnotates(fqnStack.getFqn(), fqn);
    
    return true;
  }

  @Override
  public boolean visit(SingleMemberAnnotation node) {
    // Get the fqn
    String fqn = null;
    IAnnotationBinding binding = node.resolveAnnotationBinding();
    if (binding == null) {
      fqn = "_UNKNOWN_." + node.getTypeName();
    } else {
      ITypeBinding typeBinding = binding.getAnnotationType();
      if (typeBinding == null) {
        fqn = "_UNKNOWN_." + binding.getName();
      } else {
        fqn = getTypeFqn(typeBinding);
      }
    }    
    
    // Write the annotates relation
    relationWriter.writeAnnotates(fqnStack.getFqn(), fqn);
    
    return true;
  }
  
//  @SuppressWarnings("unchecked")
  @Override
  public boolean visit(ParameterizedType node) {
//    // Write uses
//    relationWriter.writeUses(fqnStack.getFqn(), getTypeFqn(node.getType()));
//    for (Type type : (List<Type>)node.typeArguments()) {
//      try {
//        relationWriter.writeUses(fqnStack.getFqn(), getTypeFqn(type));
//      } catch (NullPointerException e) {
//        logger.log(Level.FINE, "Eclipse NPE bug in parametrized type");
//      }
//    }
    return super.visit(node);
  }
  
  @Override
  public boolean visit(SimpleType node) {
    // Write uses
    if (!fqnStack.isDeclaredTypeTop()) {
      relationWriter.writeUses(fqnStack.getFqn(), getTypeFqn(node));
    }
    return true;
  }
  
  @Override
  public boolean visit(ArrayAccess node) {
    // TODO Auto-generated method stub    
    return super.visit(node);
  }
  


  @Override
  public boolean visit(ArrayInitializer node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(ArrayType node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(AssertStatement node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(Assignment node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(Block node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(BreakStatement node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(ConditionalExpression node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(ContinueStatement node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(DoStatement node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(EmptyStatement node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(EnhancedForStatement node) {
    relationWriter.writeUses(fqnStack.getFqn(), "java.lang.Iterable");
    return super.visit(node);
  }

  @Override
  public boolean visit(ExpressionStatement node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(ForStatement node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(IfStatement node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(InfixExpression node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(LabeledStatement node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(MemberRef node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(MemberValuePair node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(MethodRef node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(MethodRefParameter node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(Modifier node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(PackageDeclaration node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(ParenthesizedExpression node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(PostfixExpression node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(PrefixExpression node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(PrimitiveType node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(QualifiedType node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(ReturnStatement node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(SwitchCase node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(SwitchStatement node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(SynchronizedStatement node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(TagElement node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(TextElement node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(ThisExpression node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(TryStatement node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(TypeDeclarationStatement node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(TypeLiteral node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(TypeParameter node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(VariableDeclarationExpression node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(VariableDeclarationStatement node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(WhileStatement node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @Override
  public boolean visit(WildcardType node) {
    // TODO Auto-generated method stub
    return super.visit(node);
  }

  @SuppressWarnings("unchecked")
  private String getFuzzyConstructorFqn(ClassInstanceCreation creation) {
    StringBuilder fqnBuilder = new StringBuilder();
    fqnBuilder.append(getTypeFqn(creation.getType())).append(".<init>");
    getFuzzyMethodArgs(fqnBuilder, creation.arguments());
    return fqnBuilder.toString();
  }
  
  @SuppressWarnings("unchecked")
  private String getFuzzyConstructorFqn(MethodDeclaration declaration) {
    StringBuilder fqnBuilder = new StringBuilder();
    fqnBuilder.append(fqnStack.getFqn()).append(".<init>");
    getFuzzyMethodParams(fqnBuilder, declaration.parameters());
    return fqnBuilder.toString();
  }

  @SuppressWarnings("unchecked")
  private String getFuzzyMethodFqn(MethodInvocation invocation) {
    StringBuilder fqnBuilder = new StringBuilder();
    fqnBuilder.append("_UNRESOLVED_.").append(invocation.getName().getFullyQualifiedName());
    getFuzzyMethodArgs(fqnBuilder, invocation.arguments());
    return fqnBuilder.toString();
  }
  
  @SuppressWarnings("unchecked")
  private String getFuzzyMethodFqn(MethodDeclaration declaration) {
    StringBuilder fqnBuilder = new StringBuilder();
    fqnBuilder.append(fqnStack.getFqn()).append('.').append(declaration.getName().getIdentifier());
    getFuzzyMethodParams(fqnBuilder, declaration.parameters());
    return fqnBuilder.toString();
  }

  private String getFuzzyMethodArgs(Iterable<Expression> arguments) {
    StringBuilder argBuilder = new StringBuilder();
    getFuzzyMethodArgs(argBuilder, arguments);
    return argBuilder.toString();
  }

  private void getFuzzyMethodArgs(StringBuilder argBuilder, Iterable<Expression> arguments) {
    boolean first = true;
    argBuilder.append('(');
    for (Expression exp : arguments) {
      ITypeBinding binding = exp.resolveTypeBinding();
      if (first) {
        first = false;
      } else {
        argBuilder.append(',');
      }
      if (binding == null) {
        argBuilder.append("_UNKNOWN_");
      } else {
        argBuilder.append(getTypeFqn(binding));
      }
    }
    argBuilder.append(')');
  }
  
  private void getFuzzyMethodParams(StringBuilder argBuilder, Iterable<SingleVariableDeclaration> parameters) {
    boolean first = true;
    argBuilder.append('(');
    for (SingleVariableDeclaration param : parameters) {
      if (first) {
        first = false;
      } else {
        argBuilder.append(',');
      }
      argBuilder.append(getTypeFqn(param.getType()));
      if (param.isVarargs()) {
        argBuilder.append("[]");
      } else if (param.getExtraDimensions() != 0) {
        argBuilder.append(BRACKETS.substring(0, 2 * param.getExtraDimensions()));
      }
    }
    argBuilder.append(')');
  }

  private String getAnonymousConstructorFqn(String classFqn, IMethodBinding binding) {
    StringBuilder fqnBuilder = new StringBuilder(classFqn);
    fqnBuilder.append(".<init>");
    getMethodArgs(fqnBuilder, binding);
    return fqnBuilder.toString();
  }
  
  private String getMethodFqn(IMethodBinding binding, boolean declaration) {
    binding = binding.getMethodDeclaration();
    StringBuilder fqnBuilder = new StringBuilder();
    ITypeBinding declaringClass = binding.getDeclaringClass();
    if (declaringClass == null) {
      logger.log(Level.SEVERE, "Unresolved declaring class for method!", binding);
    } else {
      String declaringFqn = getTypeFqn(declaringClass);
      if (declaringFqn.equals("_ERROR_")) {
        if (declaration) {
          fqnBuilder.append(fqnStack.getFqn());
        } else {
          fqnBuilder.append("_UNRESOLVED_");
        }
      } else if (declaration) {
//        if (!declaringFqn.equals(fqnStack.getFqn())) {
//          logger.log(Level.SEVERE, "Mismatch between " + declaringFqn + " and " + fqnStack.getFqn());
//        }
        fqnBuilder.append(fqnStack.getFqn());
      } else {
        fqnBuilder.append(declaringFqn);
      }
      fqnBuilder.append('.').append(binding.isConstructor() ? "<init>" : binding.getName());
    }
    getMethodArgs(fqnBuilder, binding);
    return fqnBuilder.toString();
  }

  private void getMethodArgs(StringBuilder argBuilder, IMethodBinding binding) {
    argBuilder.append('(');
    boolean first = true;
    for (ITypeBinding paramType : binding.getParameterTypes()) {
      if (first) {
        first = false;
      } else {
        argBuilder.append(',');
      }
      argBuilder.append(getTypeFqn(paramType));
    }
    argBuilder.append(')');
  }

  private static final String BRACKETS = "[][][][][][][][][][]";
 
  @SuppressWarnings("unchecked")
  private String getTypeFqn(Type type) {
    if (type == null) {
      logger.log(Level.SEVERE, "Attempt to get type fqn of null type!");
      return "_NULL_";
    }
    ITypeBinding binding = type.resolveBinding();
    if (binding == null) {
      if (type.isPrimitiveType()) {
        return ((PrimitiveType)type).getPrimitiveTypeCode().toString();
      } else if (type.isSimpleType()) {
        return "_UNRESOLVED_." + ((SimpleType) type).getName().getFullyQualifiedName();
      } else if (type.isArrayType()) {
        ArrayType arrayType = (ArrayType) type;
        Type elementType = arrayType.getElementType();
        if (elementType == null) {
          return "_UNKNOWN_" + BRACKETS.substring(0, 2 * arrayType.getDimensions());
        } else {
          return getTypeFqn(elementType) + BRACKETS.substring(0, 2 * arrayType.getDimensions());
        }
      } else if (type.isParameterizedType()) {
        ParameterizedType pType = (ParameterizedType)type;
        StringBuilder fqn = new StringBuilder(getTypeFqn(pType.getType()));
        fqn.append("<");
        boolean isFirst = true;
        for (Type arg : (List<Type>)pType.typeArguments()) {
          if (isFirst) {
            isFirst = false;
          } else {
            fqn.append(",");
          }
          try {
            fqn.append(getTypeFqn(arg));
          } catch (NullPointerException e) {
            logger.log(Level.FINE, "Eclipse NPE bug in parametrized type");
            fqn.append("_ERROR_");              
          }
        }
        fqn.append(">");
        return fqn.toString();
      } else if (type.isWildcardType()) {
        WildcardType wType = (WildcardType)type;
        Type bound = wType.getBound();
        if (bound == null) {
          return "<?>";
        } else {
          return "<?" + (wType.isUpperBound() ? "+" : "-") + getTypeFqn(bound) + ">";
        }
      } else {
        logger.log(Level.SEVERE, "Unexpected node type for unresolved type!" + type.toString());
        return "_ERROR_";
      }
    } else {
      return getTypeFqn(binding);
    }
  }

  private String getTypeFqn(ITypeBinding binding) {
	  String typeFqn = getTypeFqnRecursive(binding);
	  if (!"_ERROR_".equals(typeFqn)) {
		  
		  if (binding.getJavaElement() != null && binding.getJavaElement().getJavaProject() != null) {
			  try {
				IType type = binding.getJavaElement().getJavaProject().findType(typeFqn);
				if (type instanceof SourceType) {
					StringBuilder fqn = new StringBuilder(getProjectName(binding.getJavaElement()));
					  if (!"".equals(fqn.toString())){
						  fqn.append(".");  
					  }
					  fqn.append(typeFqn);  
					  return fqn.toString();
				}
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		  }
	  }
	  return typeFqn;
  }
  
  private String getTypeFqnRecursive(ITypeBinding binding) {
    if (binding == null) {
      return "_ERROR_";
    } else if (binding.isTypeVariable()) {
      return "<" + binding.getQualifiedName() + ">";
    } else if (binding.isPrimitive()) {
      return binding.getQualifiedName();
    } else if (binding.isArray()) {
      if (2 * binding.getDimensions() > BRACKETS.length()) {
        StringBuilder builder = new StringBuilder(getTypeFqnRecursive(binding.getElementType()));
        for (int i = 0; i < binding.getDimensions(); i++) {
          builder.append("[]");
        }
        logger.log(Level.WARNING, "Really long array! " + builder.toString() + " from " + compilationUnitPath);
        return builder.toString();
      } else {
        return getTypeFqnRecursive(binding.getElementType()) + BRACKETS.substring(0, 2 * binding.getDimensions());
      }
    } else if (binding.isAnonymous()) {
      String fqn = binding.getBinaryName();
      if (fqn == null) {
        return "_ERROR_";
      } else {
        return fqn;
      }
    } else if (binding.isLocal()) {
      String fqn = binding.getBinaryName();
      if (fqn == null) {
        if (fqnStack.isMethodTop()) {
          // TODO fix this!
          return fqnStack.getAnonymousClassFqn() + binding.getName();
        } else {
          return getTypeFqnRecursive(binding.getDeclaringClass()) + "$" + binding.getName();
        }
      } else {
        return fqn;
      }
    } else if (binding.isParameterizedType()) {
      StringBuilder fqn = new StringBuilder();
      if (binding.getErasure().isParameterizedType()) {
        logger.log(Level.SEVERE, "Parametrized type erasure is a parametrized type: " + binding.getQualifiedName());
        fqn.append("_UNRESOLVED_." + binding.getQualifiedName());
      } else {
        fqn.append(getTypeFqnRecursive(binding.getErasure()));
      }
      
      fqn.append("<");
      boolean isFirst = true;
      for (ITypeBinding arg : binding.getTypeArguments()) {
        if (isFirst) {
          isFirst = false;
        } else {
          fqn.append(",");
        }
        try {
          fqn.append(getTypeFqnRecursive(arg));
        } catch (NullPointerException e) {
          logger.log(Level.FINE, "Eclipse NPE bug in parametrized type");
          fqn.append("_ERROR_");              
        }
      }
      fqn.append(">");
      return fqn.toString();
    } else if (binding.isWildcardType()) {
      ITypeBinding bound = binding.getBound();
      if (bound == null) {
        return "<?>";
      } else {
        return "<?" + (binding.isUpperbound() ? "+" : "-") + getTypeFqnRecursive(bound) + ">";
      }
    } else {
      if (binding.isMember()) {
        return getTypeFqnRecursive(binding.getDeclaringClass()) + "$" + binding.getName();
      } else {
        if (binding.getName().equals("null")) {
          return "null";
        } else {
          String fqn = binding.getBinaryName();
          if (binding.isRecovered()) {
            if (binding.getDeclaringClass() == null || fqn == null) {
              return "_UNRESOLVED_." + binding.getName();
            } else {
              return fqn;
            }
          } else {
            return fqn;
          }
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  private String getTypeParam(TypeParameter typeParam) {
    StringBuilder builder = new StringBuilder();
    builder.append('<').append(typeParam.getName().getIdentifier());
    boolean first = true;
    for (Type bound : (List<Type>)typeParam.typeBounds()) {
      if (first) {
        first = false;
        builder.append('+');
      } else {
        builder.append('&');
      }
      builder.append(getTypeFqn(bound));
    }
    builder.append('>');
    return builder.toString();
  }
  
  private class FQNStack {
    private Deque<Enclosing> stack;

    private FQNStack() {
      stack = Helper.newStack();
    }

    public void push(String fqn, Entity type) {
      stack.push(new Enclosing(fqn, type));
    }

    public void pop() {
      stack.pop();
    }

    public String getTypeFqn(String identifier) {
      if (stack.isEmpty()) {
        return identifier;
      } else {
        return stack.peek().getFqn() + "." + identifier;
      }
    }
    public String getFqn() {
      if (stack.isEmpty()) {
        return "default";
      } else {
        return stack.peek().getFqn();
      }
    }

    public String getInitializerFqn() {
      return stack.peek().getInitializerFqn();
    }

    public String getAnonymousClassFqn() {
      for (Enclosing fqn : stack) {
        if (fqn.isNotMethod() && fqn.isNotInitializer()) {
          return fqn.getAnonymousClassFqn();
        }
      }
      return null;
    }

    public boolean isMethodTop() {
      return !stack.peek().isNotMethod();
    }
    
    public boolean isDeclaredTypeTop() {
      return stack.peek().isDeclaredType();
    }

    public int getNextParameterPos() {
      return stack.peek().getNextParameterPos();
    }
  }

  private class Enclosing {
    private String fqn;
    private Entity type;
    private int initializerCount;
    private int localClassCount;
    private int parameterCount;

    private Enclosing(String fqn, Entity type) {
      this.fqn = fqn;
      this.type = type;
      this.initializerCount = 0;
      this.localClassCount = 0;
      this.parameterCount = 0;
    }

    public String getFqn() {
      return fqn;
    }

    public String getInitializerFqn() {
      return fqn + "." + ++initializerCount;
    }

    public String getAnonymousClassFqn() {
      return fqn + "$" + ++localClassCount;
    }

    public boolean isNotMethod() {
      return !type.isMethod();
    }
    
    
    public boolean isNotInitializer() {
      return !type.isInitializer();
    }
    
    public boolean isDeclaredType() {
      return type.isDeclaredType() || type.isPackage();
    }

    public int getNextParameterPos() {
      return parameterCount++;
    }
  }
}
