package jp.co.tabocom.tsplugin.infonotepad;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import jp.co.tabocom.teratermstation.model.TargetNode;
import jp.co.tabocom.teratermstation.ui.action.TeratermStationAction;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;

public class InfoNotePadAction extends TeratermStationAction {

    private static final String SUFFIX = ".txt";

    public InfoNotePadAction(TargetNode[] nodes, Object value, Shell shell) {
        super("情報をテキストエディタで開く", "info.png", nodes, value, shell);
    }

    @Override
    public void run() {
        StringBuilder builder = new StringBuilder();
        TargetNode node = nodes[0];
        if (node.getChildren().isEmpty()) {
            // 要は子供（サーバ号機）の場合
            builder.append(node.getParent().getName());
            builder.append("\r\n");
            builder.append(node.getName());
            builder.append("\r\n");
            builder.append(String.format("%s", node.getHostName()));
            builder.append("\r\n");
            builder.append(String.format("%s", node.getIpAddr()));
            builder.append("\r\n");
            builder.append(String.format("%s", node.getLoginUsr()));
            builder.append("\r\n");
        } else {
            // 要は親（サーバ種別）の場合
            builder.append(node.getName());
            builder.append("\r\n");
            for (TargetNode nd : node.getChildren()) {
                builder.append(String.format("%s", nd.getLoginUsr()));
                builder.append("@");
                builder.append(String.format("%s", nd.getHostName()));
                builder.append(",");
                builder.append(String.format("%-15s", nd.getIpAddr()));
                builder.append(",");
                builder.append(nd.getName());
                builder.append("\r\n");
            }
        }
        try {
            File tempFile = null;
            if (node.getCategory() != null) {
                tempFile = File.createTempFile(node.getCategory().getName() + "_" + node.getName(), SUFFIX);
            } else {
                tempFile = File.createTempFile(node.getParent().getName() + "_" + node.getName(), SUFFIX);
            }
            FileWriter filewriter = new FileWriter(tempFile);
            filewriter.write(builder.toString());
            filewriter.close();
            tempFile.deleteOnExit();
            Desktop.getDesktop().edit(tempFile);
        } catch (IllegalArgumentException e) {
            MessageDialog.openError(this.shell, "情報をテキストエディタで開く", "サーバ名が短すぎて一時ファイルを生成できません。");
        } catch (IOException e) {
            MessageDialog.openError(this.shell, "情報をテキストエディタで開く", SUFFIX + "ファイルの関連付けがされていません。");
        } catch (Exception e) {
            MessageDialog.openError(this.shell, "情報をテキストエディタで開く", "問題が発生してファイルを開けません。");
        }
    }

    @Override
    public ToolTip getToolTip() {
        return null;
    }
}
