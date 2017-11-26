package ru.ezhov.panels

import java.awt.GridLayout
import java.awt.Insets
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.*
import ru.ezhov.*

class PanelFrom extends PanelDb
{
    public PanelFrom(Map<Labels, String> mapLabel)
    {
        super(mapLabel);
        setLayout(new GridBagLayout());
        Insets insets = new Insets(2, 2, 2, 2);
        add(userLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));
        add(userText, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, insets, 0, 0));

        add(passLabel, new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));
        add(passText, new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, insets, 0, 0));

        add(connectLabel, new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));
        add(connectText, new GridBagConstraints(0, 3, 2, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, insets, 0, 0));

        add(driverLabel, new GridBagConstraints(0, 4, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));
        add(driverText, new GridBagConstraints(0, 5, 2, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, insets, 0, 0));

        add(nameFieldLabel, new GridBagConstraints(0, 6, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));
        add(nameFieldText, new GridBagConstraints(0, 7, 2, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, insets, 0, 0));
        
        add(queryLabel, new GridBagConstraints(0, 8, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));
        add(panelTextPane, new GridBagConstraints(0, 9, 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0));

        setBorder(BorderFactory.createTitledBorder(mapLabel[Labels.TITLE]));
    }
}