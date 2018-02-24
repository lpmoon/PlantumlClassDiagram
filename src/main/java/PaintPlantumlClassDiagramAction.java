import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import main.Main;

import java.io.IOException;

public class PaintPlantumlClassDiagramAction extends AnAction {
    public PaintPlantumlClassDiagramAction() {
        super("plantuml.class.diagram");
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