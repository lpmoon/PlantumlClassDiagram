package com.lpmoon.plantuml.classdiagram.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.lpmoon.plantuml.classdiagram.main.Main;

import java.io.IOException;

public class PaintAllClassesAction extends AnAction {
    public PaintAllClassesAction() {
        super("com.lpmoon.plantuml.classdiagram.plantuml.class.diagram");
    }

    public void actionPerformed(AnActionEvent event) {

        Main main = new Main();

        Navigatable navigatable = event.getData(CommonDataKeys.NAVIGATABLE);

        if (navigatable != null) {
            if (navigatable instanceof PsiDirectory) {
                String path = ((PsiDirectory)navigatable).getVirtualFile().getPresentableUrl();
                try {
                    main.drawAllClasses(path, path, ((PsiDirectory)navigatable).getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}