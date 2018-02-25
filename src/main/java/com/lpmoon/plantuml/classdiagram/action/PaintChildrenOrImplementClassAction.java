package com.lpmoon.plantuml.classdiagram.action;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.lpmoon.plantuml.classdiagram.main.Main;

import java.io.IOException;

public class PaintChildrenOrImplementClassAction extends AnAction {
    public PaintChildrenOrImplementClassAction() {
        super("com.lpmoon.plantuml.classdiagram.plantuml.class.diagram");
    }

    public void actionPerformed(AnActionEvent event) {

        Main main = new Main();

        Navigatable navigatable = event.getData(CommonDataKeys.NAVIGATABLE);

        if (navigatable != null) {
            if (navigatable instanceof PsiClass) {
                Project project = event.getData(PlatformDataKeys.PROJECT);
                String projectPath = project.getBasePath();
                try {
                    main.drawImplementsOrExtends(projectPath, projectPath, ((PsiClass)navigatable).getName(), ((PsiClass)navigatable).getQualifiedName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}