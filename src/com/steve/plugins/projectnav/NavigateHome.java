package com.steve.plugins.projectnav;

import com.intellij.ide.projectView.ProjectView;
import com.intellij.ide.projectView.impl.ProjectViewImpl;
import com.intellij.ide.projectView.impl.ProjectViewTree;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class NavigateHome extends AnAction {
    public void actionPerformed(AnActionEvent event) {
        try {
            ProjectViewImpl projectView = (ProjectViewImpl) ProjectView.getInstance(event.getProject());
            ProjectViewTree tree = (ProjectViewTree) projectView.getCurrentProjectViewPane().getTree();
            tree.setSelectionRow(0);
        } catch (Exception e) {
            // ignore...
        }
    }
}
