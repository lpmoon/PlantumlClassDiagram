package com.lpmoon.plantuml.classdiagram.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiClass;
import com.lpmoon.plantuml.classdiagram.main.Main;

import java.io.IOException;

public class PaintAssociatedClassAction extends AnAction {
    public PaintAssociatedClassAction() {
        super("com.lpmoon.plantuml.classdiagram.plantuml.class.diagram.PaintAssociatedClassAction");
    }

    public void actionPerformed(AnActionEvent event) {

        Main main = new Main();

        Navigatable navigatable = event.getData(CommonDataKeys.NAVIGATABLE);

        if (navigatable != null) {

            if (navigatable instanceof PsiClass) {
                Project project = event.getData(PlatformDataKeys.PROJECT);
                String projectPath = project.getBasePath();
                try {
                    main.drawAssociated(projectPath, projectPath, ((PsiClass)navigatable).getName(), ((PsiClass)navigatable).getQualifiedName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}