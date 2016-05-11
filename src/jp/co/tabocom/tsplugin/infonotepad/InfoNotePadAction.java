package jp.co.tabocom.tsplugin.infonotepad;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;

import jp.co.tabocom.teratermstation.model.TargetNode;
import jp.co.tabocom.teratermstation.ui.action.TeratermStationAction;

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
            builder.append(String.format("%-8s", node.getHostName()));
            builder.append(",");
            builder.append(String.format("%-15s", node.getIpAddr()));
            builder.append(",");
            builder.append(node.getParent().getName());
            builder.append(",");
            builder.append(node.getName());
        } else {
            // 要は親（サーバ種別）の場合
            builder.append(node.getName());
            builder.append("\r\n");
            for (TargetNode nd : node.getChildren()) {
                builder.append(String.format("%-8s", nd.getHostName()));
                builder.append(",");
                builder.append(String.format("%-15s", nd.getIpAddr()));
                builder.append(",");
                builder.append(nd.getName());
                builder.append("\r\n");
            }
        }
        try {
            File tempFile = File.createTempFile(node.getName(), SUFFIX);
            FileWriter filewriter = new FileWriter(tempFile);
            filewriter.write(builder.toString());
            filewriter.close();
            tempFile.deleteOnExit();
            Desktop.getDesktop().edit(tempFile);
        } catch (IOException e) {
            MessageDialog.openError(this.shell, "情報をテキストエディタで開く", SUFFIX + "ファイルの関連付けがされていません。");
        }
    }

    @Override
    public ToolTip getToolTip() {
        return null;
    }
}
