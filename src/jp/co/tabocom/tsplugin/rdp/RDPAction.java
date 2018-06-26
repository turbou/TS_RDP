package jp.co.tabocom.tsplugin.rdp;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.ToolTip;

import jp.co.tabocom.teratermstation.TeratermStationShell;
import jp.co.tabocom.teratermstation.model.TargetNode;
import jp.co.tabocom.teratermstation.ui.action.TeratermStationAction;

public class RDPAction extends TeratermStationAction {

    public RDPAction(TargetNode[] nodes, Object value, TeratermStationShell shell) {
        super("リモートデスクトップで開く", null, nodes, value, shell);
    }

    @Override
    public void run() {
        TargetNode node = nodes[0];
        try {
            Runtime runtime = Runtime.getRuntime();
            // runtime.exec(new String[] { "mstsc.exe", "/v:" + node.getIpAddr(), "/user:" + node.getLoginUsr(), "/pass:" + node.getLoginPwd() });
            runtime.exec(new String[] { "mstsc.exe", "/v:" + node.getIpAddr(), "/g:10.254.96.106" });
        } catch (Exception e) {
            e.printStackTrace();
            MessageDialog.openError(this.shell, "リモートデスクトップで開く", "問題が発生してリモートデスクトップを起動できません。");
        }
    }

    @Override
    public ToolTip getToolTip() {
        return null;
    }
}
