package com.steve.plugins.projectnav;

import com.intellij.ide.projectView.ProjectView;
import com.intellij.ide.projectView.impl.ProjectViewImpl;
import com.intellij.ide.projectView.impl.ProjectViewTree;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class NavigateDown extends AnAction {
    public void actionPerformed(AnActionEvent event) {
        try {
            ProjectViewImpl projectView = (ProjectViewImpl) ProjectView.getInstance(event.getProject());
            ProjectViewTree tree = (ProjectViewTree) projectView.getCurrentProjectViewPane().getTree();
            int row = tree.getSelectionRows()[0];
            if (row < tree.getRowCount() - 1) {
                tree.setSelectionRow(row + 1);
            }
        } catch (Exception e) {
            // ignore...
        }
    }

}
