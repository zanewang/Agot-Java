package got.ui;

import java.awt.Dialog;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.apache.log4j.Logger;

public abstract class JDialogAdapter extends JDialog {

    protected final Logger logger = Logger.getLogger(this.getClass().getName());

    public JDialogAdapter(JFrame owner) {
        super(owner);
        this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        createComponent();
        layoutCoponents();
        setupListeners();
    }

    protected abstract void createComponent();

    protected abstract void layoutCoponents();

    protected abstract void setupListeners();
}
