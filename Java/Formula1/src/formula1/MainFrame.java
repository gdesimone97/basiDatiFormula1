/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formula1;

import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author 1997g
 */
public class MainFrame extends javax.swing.JFrame {

    private DefaultListModel dm = new DefaultListModel();
    private int numeroCampionato = LocalDate.now().getYear() - 1950;
    private Admin admin;

    public MainFrame() {
        initComponents();

        try {
            Query.InitConnection();

            //SETTO LE CLASSIFICHE
            settaTabellaPiloti();
            settaTabellaScuderie();
            aggiornaComboBox();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Errore di connessione");
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }

    private void aggiornaComboBox() throws SQLException {
        int anno = 2020;
        while (anno > 2018) {
            numCampionatoComboBox.addItem(Integer.toString(--anno));
        }
        
        aggiornaTabellaPiloti();
        aggiornaTabellaScuderie();
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
        DefaultTableModel defaultModel = (DefaultTableModel) tablePiloti.getModel();
        tablePiloti.setModel(defaultModel);

        int riga = 0;
        while (riga < 20) {
            defaultModel.setValueAt("", riga, 0);
            defaultModel.setValueAt("", riga, 1);
            defaultModel.setValueAt("", riga, 2);
            riga++;
            tablePiloti.setEnabled(false);
            tablePiloti.clearSelection();
        }
        
        riga = 0;
        
        ResultSet classifica;

        if (Query.isCurrent(numeroCampionato)) {
            classifica = Query.getClassificaPilotiAttuale();
        } else {
            classifica = Query.getClassifichePilotiPassati(numeroCampionato);
        }

        while (classifica.next()) {
            tablePiloti.setEnabled(true);
            defaultModel.setValueAt(riga + 1, riga, 0);
            defaultModel.setValueAt(classifica.getString("nome_pilota") + " " + classifica.getString("cognome_pilota"), riga, 1);
            defaultModel.setValueAt(classifica.getInt("punteggio"), riga, 2);
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
        DefaultTableModel defaultModel = (DefaultTableModel) tableScuderie.getModel();
        tableScuderie.removeRowSelectionInterval(0, 9);
        int riga = 0;
        
        while (riga < 10) {
            defaultModel.setValueAt("", riga, 0);
            defaultModel.setValueAt("", riga, 1);
            defaultModel.setValueAt("", riga, 2);
            riga++;
            tableScuderie.setEnabled(false);
            tableScuderie.clearSelection();
        }
        
        riga = 0;

        ResultSet classifica;
        if (Query.isCurrent(numeroCampionato)) {
            classifica = Query.getClassificaScuderieAttuale();
        } else {
            classifica = Query.getClassificheScuderiePassate(numeroCampionato);
        }

        while (classifica.next()) {
            tableScuderie.setEnabled(true);
            defaultModel.setValueAt(riga + 1, riga, 0);
            defaultModel.setValueAt(classifica.getString("nome_scuderia"), riga, 1);
            defaultModel.setValueAt(classifica.getInt("punteggio"), riga, 2);
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
        RisultatiPanel = new javax.swing.JPanel();
        verificaPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        risultatiList = new javax.swing.JList<>();
        inserimentoPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        numeroCampionatoField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        numeroGiornataField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        punteggioField = new javax.swing.JTextField();
        migliorTempoField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tempoQualificaField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        codiceField = new javax.swing.JTextField();
        ritiroCheckBox = new javax.swing.JCheckBox();
        inserisciButton = new javax.swing.JButton();
        cancellaButton = new javax.swing.JButton();
        CampionatoPanel = new javax.swing.JPanel();
        numCampionatoComboBox = new javax.swing.JComboBox<>();
        numCampionatoLabel = new javax.swing.JLabel();
        numCampionatoLabel1 = new javax.swing.JLabel();
        loginButton = new javax.swing.JButton();

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
                .addComponent(infoPilotaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(nazionalitaScuderiaTextField, javax.swing.GroupLayout.Alignment.LEADING))
                        .addContainerGap())
                    .addGroup(infoScuderiaPanelLayout.createSequentialGroup()
                        .addGroup(infoScuderiaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nazionalitaLabel1)
                            .addComponent(afferenza1Label, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(afferenza2Label, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 139, Short.MAX_VALUE))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(afferenza1Label, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(afferenza2Label, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(89, Short.MAX_VALUE))
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

        verificaPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Verifica"));

        jScrollPane1.setViewportView(risultatiList);

        javax.swing.GroupLayout verificaPanelLayout = new javax.swing.GroupLayout(verificaPanel);
        verificaPanel.setLayout(verificaPanelLayout);
        verificaPanelLayout.setHorizontalGroup(
            verificaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(verificaPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
                .addContainerGap())
        );
        verificaPanelLayout.setVerticalGroup(
            verificaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(verificaPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 39, Short.MAX_VALUE))
        );

        inserimentoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Inserimento"));

        jLabel1.setText("Numero Campionato: ");

        numeroCampionatoField.setText(" ");

        jLabel2.setText("Numero Giornata: ");

        numeroGiornataField.setText(" ");

        jLabel3.setText("Codice Pilota: ");

        punteggioField.setText(" ");

        migliorTempoField.setText(" ");

        jLabel5.setText("Punteggio:");

        jLabel4.setText("Miglior Tempo:");

        tempoQualificaField.setText(" ");

        jLabel6.setText("Tempo Qualifica:");

        codiceField.setText(" ");

        ritiroCheckBox.setText("Pilota Ritirato");

        inserisciButton.setText("Inserisci");
        inserisciButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inserisciButtonActionPerformed(evt);
            }
        });

        cancellaButton.setText("Cancella");
        cancellaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancellaButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout inserimentoPanelLayout = new javax.swing.GroupLayout(inserimentoPanel);
        inserimentoPanel.setLayout(inserimentoPanelLayout);
        inserimentoPanelLayout.setHorizontalGroup(
            inserimentoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inserimentoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inserimentoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inserimentoPanelLayout.createSequentialGroup()
                        .addGroup(inserimentoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(inserimentoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(numeroGiornataField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(numeroCampionatoField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(inserimentoPanelLayout.createSequentialGroup()
                        .addGroup(inserimentoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(inserimentoPanelLayout.createSequentialGroup()
                                .addGroup(inserimentoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel5))
                                .addGap(32, 32, 32))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, inserimentoPanelLayout.createSequentialGroup()
                                .addGroup(inserimentoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ritiroCheckBox)
                                    .addComponent(jLabel6))
                                .addGap(18, 18, 18)))
                        .addGroup(inserimentoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(migliorTempoField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(punteggioField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(codiceField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tempoQualificaField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(inserimentoPanelLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(inserisciButton)
                        .addGap(18, 18, 18)
                        .addComponent(cancellaButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        inserimentoPanelLayout.setVerticalGroup(
            inserimentoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inserimentoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inserimentoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(numeroCampionatoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(inserimentoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(numeroGiornataField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(inserimentoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(codiceField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(inserimentoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(punteggioField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(inserimentoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(migliorTempoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(inserimentoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tempoQualificaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addComponent(ritiroCheckBox)
                .addGap(18, 18, 18)
                .addGroup(inserimentoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inserisciButton)
                    .addComponent(cancellaButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout RisultatiPanelLayout = new javax.swing.GroupLayout(RisultatiPanel);
        RisultatiPanel.setLayout(RisultatiPanelLayout);
        RisultatiPanelLayout.setHorizontalGroup(
            RisultatiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RisultatiPanelLayout.createSequentialGroup()
                .addComponent(inserimentoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(verificaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        RisultatiPanelLayout.setVerticalGroup(
            RisultatiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RisultatiPanelLayout.createSequentialGroup()
                .addGroup(RisultatiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(inserimentoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(verificaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Risultati", RisultatiPanel);

        javax.swing.GroupLayout CampionatoPanelLayout = new javax.swing.GroupLayout(CampionatoPanel);
        CampionatoPanel.setLayout(CampionatoPanelLayout);
        CampionatoPanelLayout.setHorizontalGroup(
            CampionatoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 574, Short.MAX_VALUE)
        );
        CampionatoPanelLayout.setVerticalGroup(
            CampionatoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 382, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Campionato", CampionatoPanel);

        numCampionatoComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                numCampionatoComboBoxItemStateChanged(evt);
            }
        });

        numCampionatoLabel.setText("Seleziona Campionato:");

        loginButton.setText("Login Admin");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(numCampionatoLabel)
                .addGap(18, 18, 18)
                .addComponent(numCampionatoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(numCampionatoLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loginButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(numCampionatoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(numCampionatoLabel))
                        .addComponent(numCampionatoLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(loginButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(90, Short.MAX_VALUE))
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
            ResultSet rstAfferenza = Query.selezionaAfferenza(x + 20, numeroCampionato);

            while (rstScuderia.next()) {
                nomeScuderiaTextField.setText(rstScuderia.getString("nome_scuderia"));
                nazionalitaScuderiaTextField.setText(rstScuderia.getString("nazionalita_scuderia"));
                titoliScuderiaTextField.setText(rstScuderia.getString("num_campionati_vinti"));
            }

            afferenzaScuderiaLabel.setText("A questa scuderia afferiscono: ");
            rstAfferenza.next();
            ResultSet rstPilota = Query.selezionaPilota(rstAfferenza.getString("codice_pilota"));
            rstPilota.next();
            afferenza1Label.setText(rstPilota.getString("nome_pilota") + " " + rstPilota.getString("cognome_pilota"));

            rstAfferenza.next();

            rstPilota = Query.selezionaPilota(rstAfferenza.getString("codice_pilota"));
            rstPilota.next();
            afferenza2Label.setText(rstPilota.getString("nome_pilota") + " " + rstPilota.getString("cognome_pilota"));

        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tableScuderieMouseReleased

    private void cancellaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancellaButtonActionPerformed
        numeroCampionatoField.setText("");
        numeroGiornataField.setText("");
        codiceField.setText("");
        punteggioField.setText("");
        migliorTempoField.setText("");
        tempoQualificaField.setText("");
        ritiroCheckBox.setSelected(false);
    }//GEN-LAST:event_cancellaButtonActionPerformed

    private void inserisciButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inserisciButtonActionPerformed
        String numC = numeroCampionatoField.getText();
        String numG = numeroGiornataField.getText();
        String codice = codiceField.getText();
        String punti = punteggioField.getText();
        String migliorTempo = migliorTempoField.getText();
        String tempoQualifica = tempoQualificaField.getText();

        if (numC.compareTo("") == 0 || numG.compareTo("") == 0 || codice.compareTo("") == 0 || punti.compareTo("") == 0
                || migliorTempo.compareTo("") == 0 || tempoQualifica.compareTo("") == 0) {
            JOptionPane.showMessageDialog(this, "Devi inserire tutti i campi");
        } else {
            //String sede_pista = Query.selezionaSedePista(numC, numG);
            //String nome_pista = Query.selezionaNomePista(numC, numG);

            dm.addElement(numC + ":" + numG + ":" + codice + ":" + punti + ":" + migliorTempo + ":" + tempoQualifica + ":"
                    + (!ritiroCheckBox.isSelected() ? "0:" : "1:"));
            risultatiList.setModel(dm);

            codiceField.setText("");
            punteggioField.setText("");
            migliorTempoField.setText("");
            tempoQualificaField.setText("");
            ritiroCheckBox.setSelected(false);
        }


    }//GEN-LAST:event_inserisciButtonActionPerformed

    private void numCampionatoComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_numCampionatoComboBoxItemStateChanged
        numeroCampionato = Integer.parseInt((String) numCampionatoComboBox.getSelectedItem()) - 1950;
        numCampionatoLabel1.setText((Query.isCurrent(numeroCampionato) ? "Anno corrente selezionato" : "CIAO"));

        try {
            aggiornaTabellaPiloti();
            aggiornaTabellaScuderie();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_numCampionatoComboBoxItemStateChanged

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        String utente = JOptionPane.showInputDialog("Inserisci Utente: ");
        String password = JOptionPane.showInputDialog("Inserisci Password: ");
        try {
            admin = Admin.adminConnection(utente, password);
            JOptionPane.showMessageDialog(this, "Login effettuato con successo!");
            //SBLOCCA LE SESSIONI PER ADMIN
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Errore di connessione, riprova!");
        } catch (AdminLoginFailed ex) {
            JOptionPane.showMessageDialog(this, "Credenziali errate!");
        }
    }//GEN-LAST:event_loginButtonActionPerformed

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
    private javax.swing.JPanel CampionatoPanel;
    private javax.swing.JPanel PilotaPanel;
    private javax.swing.JPanel RisultatiPanel;
    private javax.swing.JPanel ScuderiaPanel;
    private javax.swing.JLabel afferenza1Label;
    private javax.swing.JLabel afferenza2Label;
    private javax.swing.JLabel afferenzaScuderiaLabel;
    private javax.swing.JButton cancellaButton;
    private javax.swing.JPanel classificaPilotaPanel;
    private javax.swing.JPanel classificaScuderiaPanel;
    private javax.swing.JTextField codiceField;
    private javax.swing.JLabel cognomeLabel;
    private javax.swing.JTextField cognomeTextField;
    private javax.swing.JLabel dataLabel;
    private javax.swing.JTextField dataTextField;
    private javax.swing.JPanel infoPilotaPanel;
    private javax.swing.JPanel infoScuderiaPanel;
    private javax.swing.JPanel inserimentoPanel;
    private javax.swing.JButton inserisciButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton loginButton;
    private javax.swing.JTextField migliorTempoField;
    private javax.swing.JLabel nazionalitaLabel;
    private javax.swing.JLabel nazionalitaLabel1;
    private javax.swing.JTextField nazionalitaScuderiaTextField;
    private javax.swing.JTextField nazionalitaTextField;
    private javax.swing.JLabel nomeLabel;
    private javax.swing.JLabel nomeLabel1;
    private javax.swing.JTextField nomeScuderiaTextField;
    private javax.swing.JTextField nomeTextField;
    private javax.swing.JComboBox<String> numCampionatoComboBox;
    private javax.swing.JLabel numCampionatoLabel;
    private javax.swing.JLabel numCampionatoLabel1;
    private javax.swing.JTextField numeroCampionatoField;
    private javax.swing.JTextField numeroGiornataField;
    private javax.swing.JTextField punteggioField;
    private javax.swing.JList<String> risultatiList;
    private javax.swing.JLabel ritiratoLabel;
    private javax.swing.JCheckBox ritiroCheckBox;
    private javax.swing.JTable tablePiloti;
    private javax.swing.JTable tableScuderie;
    private javax.swing.JTextField tempoQualificaField;
    private javax.swing.JLabel titoliLabel;
    private javax.swing.JLabel titoliLabel1;
    private javax.swing.JTextField titoliScuderiaTextField;
    private javax.swing.JTextField titoliTextField;
    private javax.swing.JPanel verificaPanel;
    // End of variables declaration//GEN-END:variables
}
