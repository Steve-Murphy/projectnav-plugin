package com.steve.plugins.projectnav;

import com.intellij.ide.projectView.ProjectView;
import com.intellij.ide.projectView.impl.ProjectViewImpl;
import com.intellij.ide.projectView.impl.ProjectViewTree;
import com.intellij.ide.projectView.impl.nodes.BasePsiNode;
import com.intellij.ide.projectView.impl.nodes.ClassTreeNode;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiElement;
import com.intellij.ui.AutoScrollFromSourceHandler;
import com.intellij.ui.AutoScrollToSourceHandler;
import com.intellij.util.Alarm;

import javax.swing.tree.DefaultMutableTreeNode;
import java.lang.reflect.Field;

public class NavigateSelect extends AnAction {
    public void actionPerformed(AnActionEvent event) {
        try {
            ProjectViewImpl projectView = (ProjectViewImpl) ProjectView.getInstance(event.getProject());
            ProjectViewTree tree = (ProjectViewTree) projectView.getCurrentProjectViewPane().getTree();

            DefaultMutableTreeNode node = tree.getSelectedNode();
            if (node.getChildCount() > 0) {
                int row = tree.getSelectionRows()[0];
                if (tree.isExpanded(row)) {
                    tree.collapseRow(row);
                } else {
                    tree.expandRow(row);
                }
            } else {
                navigateToSelectedNode(projectView, tree);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void navigateToSelectedNode(ProjectViewImpl projectView, ProjectViewTree tree) {
        Object userObject = tree.getSelectedNode().getUserObject();
        if (userObject instanceof ClassTreeNode) {
            ClassTreeNode classTreeNode = (ClassTreeNode) userObject;
            navigateWithoutAutoscrollFromSource(projectView, classTreeNode);
        } else if (userObject instanceof PsiFileNode) {
            PsiFileNode psiFileNode = (PsiFileNode) userObject;
            if (psiFileNode.canNavigate()) {
                navigateWithoutAutoscrollFromSource(projectView, psiFileNode);
            }
        }
    }

    private void navigateWithoutAutoscrollFromSource(final ProjectViewImpl projectView, BasePsiNode<? extends PsiElement> node) {
        AutoScrollToSourceHandler autoScrollToSourceHandler = null;
        AutoScrollFromSourceHandler autoScrollFromSourceHandler = null;
        Alarm autoScrollFromSourceHandlerAlarm = null;

        try {
            for (Field field : ProjectViewImpl.class.getDeclaredFields()) {
                // In emergency, BREAK GLASS.
                if (AutoScrollToSourceHandler.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    autoScrollToSourceHandler = (AutoScrollToSourceHandler) field.get(projectView);
                }

                if (AutoScrollFromSourceHandler.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    autoScrollFromSourceHandler = (AutoScrollFromSourceHandler) field.get(projectView);
                }
            }
        } catch (Exception e) {
            // oops!
        }

        try {
            for (Field field : AutoScrollFromSourceHandler.class.getDeclaredFields()) {
                // In emergency, BREAK GLASS.
                if (field.getType() == Alarm.class) {
                    field.setAccessible(true);
                    autoScrollFromSourceHandlerAlarm = (Alarm) field.get(autoScrollFromSourceHandler);
                }
            }
        } catch (Exception e) {
            // oops!
        }

        node.navigate(false);

        if (autoScrollFromSourceHandlerAlarm != null) {
            autoScrollFromSourceHandlerAlarm.cancelAllRequests();
        }

        if (autoScrollToSourceHandler != null) {
            autoScrollToSourceHandler.cancelAllRequests();
        }

    }
}
