package edu.uci.ics.sourcerer.extractor.ast;

import static edu.uci.ics.sourcerer.util.io.Logging.logger;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;

//import ca.mcgill.cs.swevo.ppa.PPAOptions;
//import ca.mcgill.cs.swevo.ppa.ui.PPAUtil;
import edu.uci.ics.sourcerer.extractor.io.WriterBundle;
import edu.uci.ics.sourcerer.util.Helper;
import edu.uci.ics.sourcerer.util.io.PropertyManager;

public final class FeatureExtractor {
  private ASTParser parser;
  private WriterBundle bundle;
  private boolean doPPA;
  
  public FeatureExtractor() {
    parser = ASTParser.newParser(AST.JLS3);
  }
  
  public void setOutput(PropertyManager properties) {
    if (bundle != null) {
      bundle.close();
    }
    bundle = new WriterBundle(properties);
  }
  
  public void setPPA(boolean doPPA) {
    this.doPPA = doPPA;
  }
  
  public void close() {
    bundle.close();
  }
  
  public void extractClassFiles(Collection<IClassFile> classFiles) {
    ClassFileExtractor extractor = new ClassFileExtractor(bundle);
    for (IClassFile classFile : classFiles) {
      try {
        extractor.extractClassFile(classFile);
      } catch (NullPointerException e) {
        logger.log(Level.SEVERE, "Unable to extract " + classFile.getElementName(), e);
      }
    }
  }
   
  public void extractSourceFiles(Collection<IFile> sourceFiles) {
    ReferenceExtractorVisitor visitor = new ReferenceExtractorVisitor(bundle);
    
    int total = 0;
    
    for (IFile source : sourceFiles) {
      if (doPPA) {
//        PPAUtil.getCU(source, new PPAOptions()).accept(visitor);
      } else {
        ICompilationUnit icu = JavaCore.createCompilationUnitFrom(source);
        
        parser.setStatementsRecovery(true);
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        parser.setSource(icu);
        
        try {
          CompilationUnit unit;
          do {
            unit = (CompilationUnit)parser.createAST(null);
          } while (reattemptCompilation(unit));
          unit.accept(visitor);
        } catch (NullPointerException e) {
          logger.log(Level.SEVERE, "Unable to create AST for " + icu.getResource().getLocation().toString(), e);
          total--;
        }
      }
      
      if (++total % 1000 == 0) {
        logger.info(total + " files extracted");
      }
    }
    
    logger.info(total + " files extracted");
  }
  
  @SuppressWarnings("unchecked")
  private boolean reattemptCompilation(CompilationUnit unit) {
    IProblem[] problems = unit.getProblems();
    if (problems.length == 0) {
      return false;
    } else {
      boolean foundIndirectRef = false;
      Collection<String> missingPrefix = Helper.newHashSet();
      Collection<String> missingRefs = Helper.newHashSet();
      // Check problems for missing indirect references
      for (IProblem problem : problems) {
        if (problem.isError()) {
          String message = problem.getMessage();
          if (message.startsWith("The type") && message.endsWith("is indirectly referenced from required .class files")) {
            foundIndirectRef = true;
            missingRefs.add(problem.getArguments()[0]);
          } else if (message.startsWith("The import") && message.endsWith("cannot be resolved")) {
            missingPrefix.add(problem.getArguments()[0]);
          }
        }
      }
      
      // Check for unresolved imports
      for (ImportDeclaration imp : (List<ImportDeclaration>)unit.imports()) {
        try {
          String name = imp.getName().getFullyQualifiedName();
          IBinding binding = imp.resolveBinding();
          if (binding == null) {
            // if there was a missing indirect reference, all bindings will be null
            if (foundIndirectRef) {
              if (isPrefix(missingPrefix, name)) {
                missingRefs.add(name);
              }
            } else {
              if (isPrefix(missingPrefix, name)) {
                // should be unresolved
                missingRefs.add(name);
              } else {
                logger.log(Level.SEVERE, "This reference should have a missing prefix: " + name);
              }
            }
          } else {
            if (!binding.getJavaElement().exists()) {
              if (isPrefix(missingPrefix, name)) {
                // should be unresolved
                missingRefs.add(name);
              } else {
                logger.log(Level.SEVERE, "This reference should have a missing prefix: " + name);
              }
            }
          }
        } catch (Exception e) {
          logger.log(Level.SEVERE, "Exception in getting binding!", e);
          String name = imp.getName().getFullyQualifiedName();
          if (isPrefix(missingPrefix, name)) {
            // should be unresolved
            missingRefs.add(name);
          } else {
            logger.log(Level.SEVERE, "This reference should have a missing prefix: " + name);
          }
        }
        
        // Let's find the jars that work
        return false;
      }
      return false;
    }
  }
  
  private boolean isPrefix(Collection<String> prefixes, String word) {
    for (String prefix : prefixes) {
      if (word.startsWith(prefix)) {
        return true;
      }
    }
    return false;
  }
}
