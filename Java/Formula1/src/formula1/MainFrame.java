/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formula1;

import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 *
 * @author 1997g
 */
public class MainFrame extends javax.swing.JFrame {

    public MainFrame() {
        initComponents();
        
        try {
            Query.InitConnection();

            //SETTO LE CLASSIFICHE
            aggiornaTabellaPiloti();
            aggiornaTabellaScuderie();
            settaTabellaPiloti();
            settaTabellaScuderie();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Errore di connessione");
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }

    private void settaTabellaPiloti() {
        TableColumnModel columnModel = tablePiloti.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(1);
        columnModel.getColumn(1).setPreferredWidth(160);
        columnModel.getColumn(2).setPreferredWidth(30);
        tablePiloti.setColumnModel(columnModel);

        JTableHeader tableheader = tablePiloti.getTableHeader();
        tableheader.setResizingAllowed(false);
        tableheader.setReorderingAllowed(false);
        tablePiloti.setTableHeader(tableheader);
    }

    private void aggiornaTabellaPiloti() throws SQLException {
        TableModel model = tablePiloti.getModel();
        int riga = 0;

        ResultSet classifica = Query.getClassificaPilotiAttuale();

        while (classifica.next()) {
            model.setValueAt(riga + 1, riga, 0);
            model.setValueAt(classifica.getString("nome_pilota") + " " + classifica.getString("cognome_pilota"), riga, 1);
            model.setValueAt(classifica.getInt("punteggio"), riga, 2);
            riga++;
        }
    }

    private void settaTabellaScuderie() {
        TableColumnModel columnModel = tableScuderie.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(1);
        columnModel.getColumn(1).setPreferredWidth(160);
        columnModel.getColumn(2).setPreferredWidth(30);
        tableScuderie.setColumnModel(columnModel);

        JTableHeader tableheader = tableScuderie.getTableHeader();
        tableheader.setResizingAllowed(false);
        tableheader.setReorderingAllowed(false);
        tableScuderie.setTableHeader(tableheader);
    }

    private void aggiornaTabellaScuderie() throws SQLException {
        TableModel model = tableScuderie.getModel();
        int riga = 0;

        ResultSet classifica = Query.getClassificaScuderieAttuale();

        while (classifica.next()) {
            model.setValueAt(riga + 1, riga, 0);
            model.setValueAt(classifica.getString("nome_scuderia"), riga, 1);
            model.setValueAt(classifica.getInt("punteggio"), riga, 2);
            riga++;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        PilotaPanel = new javax.swing.JPanel();
        classificaPilotaPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablePiloti = new javax.swing.JTable();
        infoPilotaPanel = new javax.swing.JPanel();
        nomeLabel = new javax.swing.JLabel();
        cognomeLabel = new javax.swing.JLabel();
        nazionalitaLabel = new javax.swing.JLabel();
        dataLabel = new javax.swing.JLabel();
        titoliLabel = new javax.swing.JLabel();
        ritiratoLabel = new javax.swing.JLabel();
        nomeTextField = new javax.swing.JTextField();
        cognomeTextField = new javax.swing.JTextField();
        nazionalitaTextField = new javax.swing.JTextField();
        titoliTextField = new javax.swing.JTextField();
        dataTextField = new javax.swing.JTextField();
        ScuderiaPanel = new javax.swing.JPanel();
        classificaScuderiaPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableScuderie = new javax.swing.JTable();
        infoScuderiaPanel = new javax.swing.JPanel();
        nomeLabel1 = new javax.swing.JLabel();
        nazionalitaLabel1 = new javax.swing.JLabel();
        titoliLabel1 = new javax.swing.JLabel();
        afferenzaScuderiaLabel = new javax.swing.JLabel();
        nomeScuderiaTextField = new javax.swing.JTextField();
        nazionalitaScuderiaTextField = new javax.swing.JTextField();
        titoliScuderiaTextField = new javax.swing.JTextField();
        afferenza1Label = new javax.swing.JLabel();
        afferenza2Label = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        classificaPilotaPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Classifica Piloti"));

        tablePiloti.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                " ", "Pilota", "Punti"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Short.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablePiloti.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablePiloti.setShowVerticalLines(false);
        tablePiloti.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tablePilotiMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tablePiloti);

        javax.swing.GroupLayout classificaPilotaPanelLayout = new javax.swing.GroupLayout(classificaPilotaPanel);
        classificaPilotaPanel.setLayout(classificaPilotaPanelLayout);
        classificaPilotaPanelLayout.setHorizontalGroup(
            classificaPilotaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        classificaPilotaPanelLayout.setVerticalGroup(
            classificaPilotaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        infoPilotaPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Informazioni Pilota"));

        nomeLabel.setText("Nome: ");

        cognomeLabel.setText("Cognome: ");

        nazionalitaLabel.setText("Nazionalità: ");

        dataLabel.setText("Data di nascita: ");

        titoliLabel.setText("Titoli vinti: ");

        nomeTextField.setEditable(false);
        nomeTextField.setFocusable(false);

        cognomeTextField.setEditable(false);
        cognomeTextField.setFocusable(false);

        nazionalitaTextField.setEditable(false);
        nazionalitaTextField.setFocusable(false);

        titoliTextField.setEditable(false);
        titoliTextField.setFocusable(false);

        dataTextField.setEditable(false);
        dataTextField.setFocusable(false);

        javax.swing.GroupLayout infoPilotaPanelLayout = new javax.swing.GroupLayout(infoPilotaPanel);
        infoPilotaPanel.setLayout(infoPilotaPanelLayout);
        infoPilotaPanelLayout.setHorizontalGroup(
            infoPilotaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPilotaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(infoPilotaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(infoPilotaPanelLayout.createSequentialGroup()
                        .addGroup(infoPilotaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(titoliLabel)
                            .addComponent(dataLabel)
                            .addComponent(nazionalitaLabel)
                            .addComponent(cognomeLabel)
                            .addComponent(nomeLabel))
                        .addGap(30, 30, 30)
                        .addGroup(infoPilotaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(titoliTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dataTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nazionalitaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cognomeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nomeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(52, 52, 52))
                    .addGroup(infoPilotaPanelLayout.createSequentialGroup()
                        .addComponent(ritiratoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        infoPilotaPanelLayout.setVerticalGroup(
            infoPilotaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPilotaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(infoPilotaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nomeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nomeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPilotaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cognomeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cognomeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPilotaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nazionalitaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nazionalitaLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPilotaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dataTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dataLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPilotaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(titoliTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(titoliLabel))
                .addGap(18, 18, 18)
                .addComponent(ritiratoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PilotaPanelLayout = new javax.swing.GroupLayout(PilotaPanel);
        PilotaPanel.setLayout(PilotaPanelLayout);
        PilotaPanelLayout.setHorizontalGroup(
            PilotaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PilotaPanelLayout.createSequentialGroup()
                .addComponent(classificaPilotaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(infoPilotaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        PilotaPanelLayout.setVerticalGroup(
            PilotaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PilotaPanelLayout.createSequentialGroup()
                .addGroup(PilotaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(classificaPilotaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(infoPilotaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Piloti", PilotaPanel);

        classificaScuderiaPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Classifica Scuderie"));

        tableScuderie.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                " ", "Scuderia", "Punti"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Short.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableScuderie.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableScuderie.setShowVerticalLines(false);
        tableScuderie.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tableScuderieMouseReleased(evt);
            }
        });
        jScrollPane3.setViewportView(tableScuderie);

        javax.swing.GroupLayout classificaScuderiaPanelLayout = new javax.swing.GroupLayout(classificaScuderiaPanel);
        classificaScuderiaPanel.setLayout(classificaScuderiaPanelLayout);
        classificaScuderiaPanelLayout.setHorizontalGroup(
            classificaScuderiaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        classificaScuderiaPanelLayout.setVerticalGroup(
            classificaScuderiaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        infoScuderiaPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Informazioni Scuderia"));

        nomeLabel1.setText("Nome: ");

        nazionalitaLabel1.setText("Nazionalità: ");

        titoliLabel1.setText("Titoli vinti: ");

        nomeScuderiaTextField.setEditable(false);
        nomeScuderiaTextField.setFocusable(false);

        nazionalitaScuderiaTextField.setEditable(false);
        nazionalitaScuderiaTextField.setFocusable(false);

        titoliScuderiaTextField.setEditable(false);
        titoliScuderiaTextField.setFocusable(false);

        javax.swing.GroupLayout infoScuderiaPanelLayout = new javax.swing.GroupLayout(infoScuderiaPanel);
        infoScuderiaPanel.setLayout(infoScuderiaPanelLayout);
        infoScuderiaPanelLayout.setHorizontalGroup(
            infoScuderiaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoScuderiaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(infoScuderiaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(afferenzaScuderiaLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, infoScuderiaPanelLayout.createSequentialGroup()
                        .addGroup(infoScuderiaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(titoliScuderiaTextField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nomeScuderiaTextField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, infoScuderiaPanelLayout.createSequentialGroup()
                                .addGroup(infoScuderiaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(nomeLabel1)
                                    .addComponent(titoliLabel1))
                                .addGap(0, 194, Short.MAX_VALUE))
                            .addComponent(nazionalitaScuderiaTextField, javax.swing.GroupLayout.Alignment.LEADING))
                        .addContainerGap())
                    .addGroup(infoScuderiaPanelLayout.createSequentialGroup()
                        .addGroup(infoScuderiaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nazionalitaLabel1)
                            .addComponent(afferenza1Label, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(afferenza2Label, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        infoScuderiaPanelLayout.setVerticalGroup(
            infoScuderiaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoScuderiaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nomeLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nomeScuderiaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nazionalitaLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nazionalitaScuderiaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(titoliLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(titoliScuderiaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(afferenzaScuderiaLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(afferenza1Label, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(afferenza2Label, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(73, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout ScuderiaPanelLayout = new javax.swing.GroupLayout(ScuderiaPanel);
        ScuderiaPanel.setLayout(ScuderiaPanelLayout);
        ScuderiaPanelLayout.setHorizontalGroup(
            ScuderiaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ScuderiaPanelLayout.createSequentialGroup()
                .addComponent(classificaScuderiaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(infoScuderiaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        ScuderiaPanelLayout.setVerticalGroup(
            ScuderiaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ScuderiaPanelLayout.createSequentialGroup()
                .addGroup(ScuderiaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(classificaScuderiaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(infoScuderiaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 25, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Scuderie", ScuderiaPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(130, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tablePilotiMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePilotiMouseReleased
        try {
            ResultSet rst = Query.selezionaPilota(tablePiloti.getSelectedRow());

            while (rst.next()) {
                nomeTextField.setText(rst.getString("nome_pilota"));
                cognomeTextField.setText(rst.getString("cognome_pilota"));
                nazionalitaTextField.setText(rst.getString("nazionalita"));
                dataTextField.setText(rst.getString("data_nascita"));
                titoliTextField.setText(rst.getString("titoli_vinti"));

                if (rst.getBoolean("attivo")) {
                    ritiratoLabel.setText("Attuale posizione in classifica: " + (tablePiloti.getSelectedRow() + 1));
                } else {
                    ritiratoLabel.setText("Questo pilota si è ritirato nel: " + rst.getInt("data_ritiro"));
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tablePilotiMouseReleased

    private void tableScuderieMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableScuderieMouseReleased
        try {
            int x = tableScuderie.getSelectedRow();
            ResultSet rstScuderia = Query.selezionaScuderia(x);
            ResultSet rstAfferenza = Query.selezionaAfferenza(x + 20);

            while (rstScuderia.next()) {
                nomeScuderiaTextField.setText(rstScuderia.getString("nome_scuderia"));
                nazionalitaScuderiaTextField.setText(rstScuderia.getString("nazionalita_scuderia"));
                titoliScuderiaTextField.setText(rstScuderia.getString("num_campionati_vinti"));

                afferenzaScuderiaLabel.setText("A questa scuderia afferiscono: ");
                while (rstAfferenza.next()) {
                    ResultSet rstPilota = Query.selezionaPilota(rstAfferenza.getString("codice_pilota"));
                    while (rstPilota.next()) {
                        afferenza1Label.setText(rstPilota.getString("nome_pilota") + " " + rstPilota.getString("cognome_pilota"));
                    }
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tableScuderieMouseReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PilotaPanel;
    private javax.swing.JPanel ScuderiaPanel;
    private javax.swing.JLabel afferenza1Label;
    private javax.swing.JLabel afferenza2Label;
    private javax.swing.JLabel afferenzaScuderiaLabel;
    private javax.swing.JPanel classificaPilotaPanel;
    private javax.swing.JPanel classificaScuderiaPanel;
    private javax.swing.JLabel cognomeLabel;
    private javax.swing.JTextField cognomeTextField;
    private javax.swing.JLabel dataLabel;
    private javax.swing.JTextField dataTextField;
    private javax.swing.JPanel infoPilotaPanel;
    private javax.swing.JPanel infoScuderiaPanel;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel nazionalitaLabel;
    private javax.swing.JLabel nazionalitaLabel1;
    private javax.swing.JTextField nazionalitaScuderiaTextField;
    private javax.swing.JTextField nazionalitaTextField;
    private javax.swing.JLabel nomeLabel;
    private javax.swing.JLabel nomeLabel1;
    private javax.swing.JTextField nomeScuderiaTextField;
    private javax.swing.JTextField nomeTextField;
    private javax.swing.JLabel ritiratoLabel;
    private javax.swing.JTable tablePiloti;
    private javax.swing.JTable tableScuderie;
    private javax.swing.JLabel titoliLabel;
    private javax.swing.JLabel titoliLabel1;
    private javax.swing.JTextField titoliScuderiaTextField;
    private javax.swing.JTextField titoliTextField;
    // End of variables declaration//GEN-END:variables
}
