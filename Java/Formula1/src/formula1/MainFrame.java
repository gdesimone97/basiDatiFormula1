/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formula1;

import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
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
            settaTabellaCalendario();
            settaTabellaRisultati();
            aggiornaComboBox();
            logoutButton.setVisible(false);

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
        aggiornaCalendario();
    }

    private void aggiornaCalendario() throws SQLException {
        DefaultTableModel defaultModel = (DefaultTableModel) tableCalendario.getModel();
        tableCalendario.setModel(defaultModel);

        int riga = 0;
        while (riga < 21) {
            defaultModel.setValueAt("", riga, 0);
            defaultModel.setValueAt("", riga, 1);
            defaultModel.setValueAt("", riga, 2);
            riga++;
            tableCalendario.setEnabled(false);
            tableCalendario.clearSelection();
        }

        riga = 0;

        ResultSet calendario = Query.getCalendario(numeroCampionato);

        while (calendario.next()) {
            tableCalendario.setEnabled(true);
            defaultModel.setValueAt(calendario.getString("numero_giornata"), riga, 0);
            defaultModel.setValueAt(calendario.getString("sede_pista"), riga, 1);
            defaultModel.setValueAt(calendario.getString("nome_pista"), riga, 2);
            riga++;
        }
    }

    private void settaTabellaCalendario() {
        TableColumnModel columnModel = tableCalendario.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(1);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(200);
        tableCalendario.setColumnModel(columnModel);

        JTableHeader tableheader = tableCalendario.getTableHeader();
        tableheader.setResizingAllowed(false);
        tableheader.setReorderingAllowed(false);
        tableCalendario.setTableHeader(tableheader);
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

    private void settaTabellaRisultati() {
        TableColumnModel columnModel = tableRisultati.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(200);
        columnModel.getColumn(1).setPreferredWidth(50);
        columnModel.getColumn(2).setPreferredWidth(50);
        columnModel.getColumn(3).setPreferredWidth(20);
        columnModel.getColumn(4).setPreferredWidth(10);
        tableRisultati.setColumnModel(columnModel);

        JTableHeader tableheader = tableRisultati.getTableHeader();
        tableheader.setResizingAllowed(false);
        tableheader.setReorderingAllowed(false);
        tableRisultati.setTableHeader(tableheader);
    }

    private void aggiornaTabellaRisultati() {
        DefaultTableModel defaultModel = (DefaultTableModel) tableRisultati.getModel();
        tableRisultati.removeRowSelectionInterval(0, 19);
        int riga = 0;
        while (riga < 20) {
            defaultModel.setValueAt("", riga, 0);
            defaultModel.setValueAt("", riga, 1);
            defaultModel.setValueAt("", riga, 2);
            defaultModel.setValueAt("", riga, 3);
            defaultModel.setValueAt(false, riga, 4);
            riga++;
            tableRisultati.clearSelection();
        }
        numGiornataComboBox.setSelectedIndex(0);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        adminFrame = new javax.swing.JFrame();
        logoutButton = new javax.swing.JButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        RisultatiAdminPanel = new javax.swing.JPanel();
        verificaPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        risultatiList = new javax.swing.JList<>();
        cancellaTuttoButton = new javax.swing.JButton();
        cancellaRigaButton = new javax.swing.JButton();
        commitButton = new javax.swing.JButton();
        scriviFileButton = new javax.swing.JButton();
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
        countRisultatiLabel = new javax.swing.JLabel();
        countRisultatiLabel2 = new javax.swing.JLabel();
        inserisciDaFileButton = new javax.swing.JButton();
        CampionatoAdminPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
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
        personaleButton = new javax.swing.JButton();
        CalendarioPanel = new javax.swing.JPanel();
        calendariPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableCalendario = new javax.swing.JTable();
        infoPistaPanel = new javax.swing.JPanel();
        lunghezzaPistaLabel = new javax.swing.JLabel();
        lunghezzaTextField = new javax.swing.JTextField();
        curveLabel = new javax.swing.JLabel();
        curveTextField = new javax.swing.JTextField();
        inaugurazioneLabel = new javax.swing.JLabel();
        inaugurazioneTextField = new javax.swing.JTextField();
        recordLabel = new javax.swing.JLabel();
        recordTextField = new javax.swing.JTextField();
        RisultatiPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        numGiornataComboBox = new javax.swing.JComboBox<>();
        pistaNumGiornata = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableRisultati = new javax.swing.JTable();
        numCampionatoComboBox = new javax.swing.JComboBox<>();
        numCampionatoLabel = new javax.swing.JLabel();
        loginButton = new javax.swing.JButton();

        adminFrame.setResizable(false);
        adminFrame.setSize(new java.awt.Dimension(574, 500));
        adminFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                adminFrameWindowClosing(evt);
            }
        });

        logoutButton.setText("Logout Admin");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        verificaPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Verifica"));

        risultatiList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        risultatiList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                risultatiListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(risultatiList);

        cancellaTuttoButton.setText("Cancella tutto");
        cancellaTuttoButton.setEnabled(false);
        cancellaTuttoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancellaTuttoButtonActionPerformed(evt);
            }
        });

        cancellaRigaButton.setText("Cancella");
        cancellaRigaButton.setEnabled(false);
        cancellaRigaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancellaRigaButtonActionPerformed(evt);
            }
        });

        commitButton.setText("Commit");
        commitButton.setEnabled(false);
        commitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commitButtonActionPerformed(evt);
            }
        });

        scriviFileButton.setText("Scrivi su file");
        scriviFileButton.setEnabled(false);
        scriviFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scriviFileButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout verificaPanelLayout = new javax.swing.GroupLayout(verificaPanel);
        verificaPanel.setLayout(verificaPanelLayout);
        verificaPanelLayout.setHorizontalGroup(
            verificaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(verificaPanelLayout.createSequentialGroup()
                .addGroup(verificaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(verificaPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(verificaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(commitButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(scriviFileButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(verificaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cancellaRigaButton, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cancellaTuttoButton, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        verificaPanelLayout.setVerticalGroup(
            verificaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(verificaPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addGroup(verificaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(scriviFileButton)
                    .addComponent(cancellaRigaButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(verificaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancellaTuttoButton)
                    .addComponent(commitButton))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        inserimentoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Inserimento"));

        jLabel1.setText("Numero Campionato: ");

        jLabel2.setText("Numero Giornata: ");

        jLabel3.setText("Codice Pilota: ");

        jLabel5.setText("Punteggio:");

        jLabel4.setText("Miglior Tempo:");

        jLabel6.setText("Tempo Qualifica:");

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

        countRisultatiLabel.setText("Risultati inseriti: ");

        countRisultatiLabel2.setText("0");

        inserisciDaFileButton.setText("Inserisci da file");
        inserisciDaFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inserisciDaFileButtonActionPerformed(evt);
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
                        .addComponent(countRisultatiLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(countRisultatiLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(inserimentoPanelLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(inserimentoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inserisciDaFileButton)
                            .addGroup(inserimentoPanelLayout.createSequentialGroup()
                                .addComponent(inserisciButton)
                                .addGap(18, 18, 18)
                                .addComponent(cancellaButton)))))
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
                .addGap(18, 18, 18)
                .addComponent(inserisciDaFileButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(inserimentoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(countRisultatiLabel)
                    .addComponent(countRisultatiLabel2))
                .addGap(21, 21, 21))
        );

        javax.swing.GroupLayout RisultatiAdminPanelLayout = new javax.swing.GroupLayout(RisultatiAdminPanel);
        RisultatiAdminPanel.setLayout(RisultatiAdminPanelLayout);
        RisultatiAdminPanelLayout.setHorizontalGroup(
            RisultatiAdminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RisultatiAdminPanelLayout.createSequentialGroup()
                .addComponent(inserimentoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(verificaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        RisultatiAdminPanelLayout.setVerticalGroup(
            RisultatiAdminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RisultatiAdminPanelLayout.createSequentialGroup()
                .addGroup(RisultatiAdminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(inserimentoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(verificaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Risultati", RisultatiAdminPanel);

        javax.swing.GroupLayout CampionatoAdminPanelLayout = new javax.swing.GroupLayout(CampionatoAdminPanel);
        CampionatoAdminPanel.setLayout(CampionatoAdminPanelLayout);
        CampionatoAdminPanelLayout.setHorizontalGroup(
            CampionatoAdminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 569, Short.MAX_VALUE)
        );
        CampionatoAdminPanelLayout.setVerticalGroup(
            CampionatoAdminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 379, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Campionato", CampionatoAdminPanel);

        jLabel7.setText("Benvenuto nell'interfaccia amministratore!");

        javax.swing.GroupLayout adminFrameLayout = new javax.swing.GroupLayout(adminFrame.getContentPane());
        adminFrame.getContentPane().setLayout(adminFrameLayout);
        adminFrameLayout.setHorizontalGroup(
            adminFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminFrameLayout.createSequentialGroup()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, adminFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logoutButton)
                .addContainerGap())
        );
        adminFrameLayout.setVerticalGroup(
            adminFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, adminFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(adminFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(logoutButton)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

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
                .addGap(136, 136, 136))
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

        personaleButton.setText("Mostra Personale");
        personaleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                personaleButtonActionPerformed(evt);
            }
        });

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
                            .addComponent(afferenza2Label, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(personaleButton))
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
                .addGap(18, 18, 18)
                .addComponent(personaleButton)
                .addContainerGap(48, Short.MAX_VALUE))
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
                .addGap(0, 69, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Scuderie", ScuderiaPanel);

        calendariPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Calendario"));

        tableCalendario.setModel(new javax.swing.table.DefaultTableModel(
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
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "", "Sede Pista", "Nome Pista"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Short.class, java.lang.String.class, java.lang.String.class
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
        tableCalendario.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableCalendario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tableCalendarioMouseReleased(evt);
            }
        });
        jScrollPane4.setViewportView(tableCalendario);

        javax.swing.GroupLayout calendariPanelLayout = new javax.swing.GroupLayout(calendariPanel);
        calendariPanel.setLayout(calendariPanelLayout);
        calendariPanelLayout.setHorizontalGroup(
            calendariPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        calendariPanelLayout.setVerticalGroup(
            calendariPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(calendariPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 19, Short.MAX_VALUE))
        );

        infoPistaPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Informazioni Pista"));

        lunghezzaPistaLabel.setText("Lunghezza:");

        lunghezzaTextField.setEditable(false);

        curveLabel.setText("Numero Curve:");

        curveTextField.setEditable(false);

        inaugurazioneLabel.setText("Anno Inaugurazione:");

        inaugurazioneTextField.setEditable(false);

        recordLabel.setText("Record Su Pista:");

        recordTextField.setEditable(false);

        javax.swing.GroupLayout infoPistaPanelLayout = new javax.swing.GroupLayout(infoPistaPanel);
        infoPistaPanel.setLayout(infoPistaPanelLayout);
        infoPistaPanelLayout.setHorizontalGroup(
            infoPistaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPistaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(infoPistaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lunghezzaTextField)
                    .addComponent(curveTextField)
                    .addComponent(inaugurazioneTextField)
                    .addGroup(infoPistaPanelLayout.createSequentialGroup()
                        .addGroup(infoPistaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lunghezzaPistaLabel)
                            .addComponent(curveLabel)
                            .addComponent(inaugurazioneLabel)
                            .addComponent(recordLabel))
                        .addGap(0, 51, Short.MAX_VALUE))
                    .addComponent(recordTextField))
                .addContainerGap())
        );
        infoPistaPanelLayout.setVerticalGroup(
            infoPistaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPistaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lunghezzaPistaLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lunghezzaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(curveLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(curveTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(inaugurazioneLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inaugurazioneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(recordLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(recordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout CalendarioPanelLayout = new javax.swing.GroupLayout(CalendarioPanel);
        CalendarioPanel.setLayout(CalendarioPanelLayout);
        CalendarioPanelLayout.setHorizontalGroup(
            CalendarioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CalendarioPanelLayout.createSequentialGroup()
                .addComponent(calendariPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(infoPistaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        CalendarioPanelLayout.setVerticalGroup(
            CalendarioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CalendarioPanelLayout.createSequentialGroup()
                .addGroup(CalendarioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(calendariPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(infoPistaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Calendario", CalendarioPanel);

        jLabel8.setText("Seleziona una giornata: ");

        numGiornataComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELEZIONA", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21" }));
        numGiornataComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                numGiornataComboBoxItemStateChanged(evt);
            }
        });

        tableRisultati.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Pilota", "Miglior Tempo", "Tempo Qualifica", "Punti", "Ritiro"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableRisultati.setRowSelectionAllowed(false);
        jScrollPane5.setViewportView(tableRisultati);

        javax.swing.GroupLayout RisultatiPanelLayout = new javax.swing.GroupLayout(RisultatiPanel);
        RisultatiPanel.setLayout(RisultatiPanelLayout);
        RisultatiPanelLayout.setHorizontalGroup(
            RisultatiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RisultatiPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(RisultatiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)
                    .addComponent(pistaNumGiornata, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, RisultatiPanelLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(numGiornataComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        RisultatiPanelLayout.setVerticalGroup(
            RisultatiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RisultatiPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(RisultatiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(numGiornataComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pistaNumGiornata, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Risultati", RisultatiPanel);

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(loginButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numCampionatoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numCampionatoLabel)
                    .addComponent(loginButton))
                .addGap(7, 7, 7)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tablePilotiMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePilotiMouseReleased
        try {
            ResultSet rst = Query.selezionaPilota(tablePiloti.getSelectedRow(), numeroCampionato);

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
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(this, "Spiacenti, non sono presenti informazioni per questo campionato!");
        }
    }//GEN-LAST:event_tablePilotiMouseReleased

    private void tableScuderieMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableScuderieMouseReleased
        try {
            int x = tableScuderie.getSelectedRow();
            ResultSet rstScuderia = Query.selezionaScuderia(x, numeroCampionato);
            ResultSet rstAfferenza = Query.selezionaAfferenza(x + 20, numeroCampionato);

            while (rstScuderia.next()) {
                personaleButton.setEnabled(true);
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
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(this, "Spiacenti, non sono presenti informazioni per questo campionato!");
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
        try {
            String numC = numeroCampionatoField.getText();
            String numG = numeroGiornataField.getText();
            String codice = codiceField.getText();
            String punti = punteggioField.getText();
            String migliorTempo = migliorTempoField.getText();
            String tempoQualifica = tempoQualificaField.getText();
            String sede_pista;
            String nome_pista;

            if (numC.compareTo("") == 0 || numG.compareTo("") == 0 || codice.compareTo("") == 0 || punti.compareTo("") == 0
                    || migliorTempo.compareTo("") == 0 || tempoQualifica.compareTo("") == 0) {
                JOptionPane.showMessageDialog(this, "Devi inserire tutti i campi");

            } else {
                ResultSet rst = Query.selezionaPista(Integer.parseInt(numC), Integer.parseInt(numG));
                rst.next();
                sede_pista = rst.getString("sede_pista");
                nome_pista = rst.getString("nome_pista");

                dm.addElement(sede_pista + ":" + nome_pista + ":" + codice + ":" + numC + ":" + punti + ":" + migliorTempo + ":" + tempoQualifica + ":"
                        + (!ritiroCheckBox.isSelected() ? "0:" : "1:"));
                risultatiList.setModel(dm);

                codiceField.setText("");
                punteggioField.setText("");
                migliorTempoField.setText("");
                tempoQualificaField.setText("");
                ritiroCheckBox.setSelected(false);

                Integer count = Integer.parseInt(countRisultatiLabel2.getText()) + 1;
                countRisultatiLabel2.setText(count.toString());

                if (dm.getSize() == 20) {
                    commitButton.setEnabled(true);
                    cancellaTuttoButton.setEnabled(true);
                    inserisciButton.setEnabled(false);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Non riesco a trovare la pista!");
        }


    }//GEN-LAST:event_inserisciButtonActionPerformed

    private void numCampionatoComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_numCampionatoComboBoxItemStateChanged
        numeroCampionato = Integer.parseInt((String) numCampionatoComboBox.getSelectedItem()) - 1950;

        nomeTextField.setText("");
        cognomeTextField.setText("");
        nazionalitaTextField.setText("");
        dataTextField.setText("");
        titoliTextField.setText("");
        ritiratoLabel.setText("");
        nomeScuderiaTextField.setText("");
        nazionalitaScuderiaTextField.setText("");
        titoliScuderiaTextField.setText("");
        afferenzaScuderiaLabel.setText("");
        afferenza1Label.setText("");
        afferenza2Label.setText("");
        personaleButton.setEnabled(false);
        lunghezzaTextField.setText("");
        curveTextField.setText("");
        inaugurazioneTextField.setText("");
        recordTextField.setText("");

        try {
            aggiornaTabellaPiloti();
            aggiornaTabellaScuderie();
            aggiornaCalendario();
            aggiornaTabellaRisultati();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_numCampionatoComboBoxItemStateChanged

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        String utente = JOptionPane.showInputDialog("Inserisci Utente: ");
        if (utente != null) {
            String password = JOptionPane.showInputDialog("Inserisci Password: ");
            if (password != null) {
                try {
                    admin = Admin.adminConnection(utente, password);
                    JOptionPane.showMessageDialog(this, "Login effettuato con successo!");
                    logoutButton.setVisible(true);
                    loginButton.setVisible(false);
                    adminFrame.setVisible(true);

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Errore di connessione, riprova!");
                } catch (AdminLoginFailed ex) {
                    JOptionPane.showMessageDialog(this, "Credenziali errate!");
                }
            }
        }
    }//GEN-LAST:event_loginButtonActionPerformed

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        admin.logOut(admin);
        logoutButton.setVisible(false);
        loginButton.setVisible(true);
        adminFrame.setVisible(false);

    }//GEN-LAST:event_logoutButtonActionPerformed

    private void personaleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_personaleButtonActionPerformed
        try {
            int x = tableScuderie.getSelectedRow();
            ResultSet rstScuderia = Query.selezionaScuderia(x, numeroCampionato);
            rstScuderia.next();
            ResultSet rst[] = Query.selezionaPersonale(numeroCampionato, rstScuderia.getString("nome_scuderia"));

            String str = "";
            str += "AMMINISTRATORE DELEGATO SCUDERIA: \n";
            while (rst[0].next()) {
                str += rst[0].getString("nazionalita_personale") + " ";
                str += rst[0].getString("data_nascita") + " ";
                str += rst[0].getString("professione") + " ";
                str += rst[0].getString("nome_personale") + " ";
                str += rst[0].getString("cognome_personale") + "\n";
            }

            str += "\nPERSONALE SCUDERIA: \n";

            while (rst[1].next()) {
                str += rst[1].getString("nazionalita_personale") + " ";
                str += rst[1].getString("data_nascita") + " ";
                str += rst[1].getString("professione") + " ";
                str += rst[1].getString("nome_personale") + " ";
                str += rst[1].getString("cognome_personale") + "\n";
            }

            JOptionPane.showMessageDialog(this, str);

        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_personaleButtonActionPerformed

    private void commitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_commitButtonActionPerformed
        try {
            admin.inserisciRisultati("risultati.txt");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Errore nell'inserimento su database. Riprova");
        }
    }//GEN-LAST:event_commitButtonActionPerformed

    private void risultatiListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_risultatiListValueChanged
        if (!risultatiList.isSelectionEmpty()) {
            cancellaRigaButton.setEnabled(true);
            scriviFileButton.setEnabled(true);
        } else {
            cancellaRigaButton.setEnabled(false);
            scriviFileButton.setEnabled(false);
        }

    }//GEN-LAST:event_risultatiListValueChanged

    private void cancellaRigaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancellaRigaButtonActionPerformed
        dm.removeElementAt(risultatiList.getSelectedIndex());
        Integer count = Integer.parseInt(countRisultatiLabel2.getText()) - 1;
        countRisultatiLabel2.setText(count.toString());
        inserisciButton.setEnabled(true);
    }//GEN-LAST:event_cancellaRigaButtonActionPerformed

    private void cancellaTuttoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancellaTuttoButtonActionPerformed
        cancellaTuttoButton.setEnabled(false);
        commitButton.setEnabled(false);
        countRisultatiLabel2.setText("0");
        inserisciButton.setEnabled(true);

        for (Object o : dm.toArray())
            dm.removeAllElements();
    }//GEN-LAST:event_cancellaTuttoButtonActionPerformed

    private void inserisciDaFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inserisciDaFileButtonActionPerformed
        try {
            Integer count = 0;
            int x = jFileChooser1.showOpenDialog(this);
            String nomeFile = "";
            if (x == JFileChooser.APPROVE_OPTION) {
                nomeFile = jFileChooser1.getSelectedFile().getName();
            }

            Scanner sc = new Scanner(new BufferedReader(new FileReader(nomeFile)));
            sc.useDelimiter("");

            while (sc.hasNext()) {
                dm.addElement(sc.nextLine());
                count++;
            }
            risultatiList.setModel(dm);
            countRisultatiLabel2.setText(count.toString());
            commitButton.setEnabled(true);
            cancellaTuttoButton.setEnabled(true);

        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Errore nell'apertura del file.");
        }
    }//GEN-LAST:event_inserisciDaFileButtonActionPerformed

    private void scriviFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scriviFileButtonActionPerformed
        try (BufferedWriter w = new BufferedWriter(new FileWriter("risultati.txt", true))) {
            for (Object o : dm.toArray()) {
                w.write(o.toString());
                w.newLine();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Errore nell'apertura del file, riprova.");
        }
    }//GEN-LAST:event_scriviFileButtonActionPerformed

    private void tableCalendarioMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCalendarioMouseReleased
        try {
            int x = tableCalendario.getSelectedRow() + 1;
            ResultSet rstGiornata = Query.selezionaGiornata(numeroCampionato, x);

            while (rstGiornata.next()) {
                String sede_pista = rstGiornata.getString("sede_pista");
                String nome_pista = rstGiornata.getString("nome_pista");
                ResultSet rstPista = Query.selezionaPista(sede_pista, nome_pista);
                rstPista.next();
                lunghezzaTextField.setText(((Integer) rstPista.getInt("lunghezza")).toString() + " metri");
                curveTextField.setText(((Integer) rstPista.getInt("num_curve")).toString());
                inaugurazioneTextField.setText(((Integer) rstPista.getInt("anno_inaugurazione")).toString());
                Integer record = rstPista.getInt("giro_veloce");
                if (record != 0) {
                    Integer millis = record % 1000;
                    String millisec;
                    if (millis < 10) {
                        millisec = "00" + millis.toString();
                    } else if (millis < 100) {
                        millisec = "0" + millis.toString();
                    } else {
                        millisec = millis.toString();
                    }
                    Integer secondi = (record / 1000) % 60;
                    Integer minuti = (record / 1000) / 60;
                    recordTextField.setText(minuti.toString() + ":" + (secondi < 10 ? "0" + secondi : secondi) + ":" + millisec);
                } else {
                    recordTextField.setText("Nessun record");
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(this, "Spiacenti, non sono presenti informazioni per questo campionato!");
        }
    }//GEN-LAST:event_tableCalendarioMouseReleased

    private void adminFrameWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_adminFrameWindowClosing
        admin.logOut(admin);
        logoutButton.setVisible(false);
        loginButton.setVisible(true);
        adminFrame.setVisible(false);
    }//GEN-LAST:event_adminFrameWindowClosing

    private void numGiornataComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_numGiornataComboBoxItemStateChanged
        try {
            Integer numeroGiornata = numGiornataComboBox.getSelectedIndex();

            if (numeroGiornata != 0) {
                ResultSet rstPista = Query.selezionaPista(numeroCampionato, numeroGiornata);
                pistaNumGiornata.setText("");
                while (rstPista.next()) {
                    pistaNumGiornata.setText("Città: " + rstPista.getString("sede_pista") + "           Pista: " + rstPista.getString("nome_pista"));
                }

//            ResultSet risultati = Query.getRisultati(numeroCampionato, numeroGiornata);
//            int count = 0;
//            while(risultati.next()) {
//                aggiornaTabellaRisultati();
//                count++;
//            }
//            
//            if(count==0)
//                JOptionPane.showMessageDialog(this, "Spiacenti, non ho trovato risultati per questa giornata");
            } else {
                pistaNumGiornata.setText("");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_numGiornataComboBoxItemStateChanged

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
    private javax.swing.JPanel CalendarioPanel;
    private javax.swing.JPanel CampionatoAdminPanel;
    private javax.swing.JPanel PilotaPanel;
    private javax.swing.JPanel RisultatiAdminPanel;
    private javax.swing.JPanel RisultatiPanel;
    private javax.swing.JPanel ScuderiaPanel;
    private javax.swing.JFrame adminFrame;
    private javax.swing.JLabel afferenza1Label;
    private javax.swing.JLabel afferenza2Label;
    private javax.swing.JLabel afferenzaScuderiaLabel;
    private javax.swing.JPanel calendariPanel;
    private javax.swing.JButton cancellaButton;
    private javax.swing.JButton cancellaRigaButton;
    private javax.swing.JButton cancellaTuttoButton;
    private javax.swing.JPanel classificaPilotaPanel;
    private javax.swing.JPanel classificaScuderiaPanel;
    private javax.swing.JTextField codiceField;
    private javax.swing.JLabel cognomeLabel;
    private javax.swing.JTextField cognomeTextField;
    private javax.swing.JButton commitButton;
    private javax.swing.JLabel countRisultatiLabel;
    private javax.swing.JLabel countRisultatiLabel2;
    private javax.swing.JLabel curveLabel;
    private javax.swing.JTextField curveTextField;
    private javax.swing.JLabel dataLabel;
    private javax.swing.JTextField dataTextField;
    private javax.swing.JLabel inaugurazioneLabel;
    private javax.swing.JTextField inaugurazioneTextField;
    private javax.swing.JPanel infoPilotaPanel;
    private javax.swing.JPanel infoPistaPanel;
    private javax.swing.JPanel infoScuderiaPanel;
    private javax.swing.JPanel inserimentoPanel;
    private javax.swing.JButton inserisciButton;
    private javax.swing.JButton inserisciDaFileButton;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JButton loginButton;
    private javax.swing.JButton logoutButton;
    private javax.swing.JLabel lunghezzaPistaLabel;
    private javax.swing.JTextField lunghezzaTextField;
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
    private javax.swing.JComboBox<String> numGiornataComboBox;
    private javax.swing.JTextField numeroCampionatoField;
    private javax.swing.JTextField numeroGiornataField;
    private javax.swing.JButton personaleButton;
    private javax.swing.JLabel pistaNumGiornata;
    private javax.swing.JTextField punteggioField;
    private javax.swing.JLabel recordLabel;
    private javax.swing.JTextField recordTextField;
    private javax.swing.JList<String> risultatiList;
    private javax.swing.JLabel ritiratoLabel;
    private javax.swing.JCheckBox ritiroCheckBox;
    private javax.swing.JButton scriviFileButton;
    private javax.swing.JTable tableCalendario;
    private javax.swing.JTable tablePiloti;
    private javax.swing.JTable tableRisultati;
    private javax.swing.JTable tableScuderie;
    private javax.swing.JTextField tempoQualificaField;
    private javax.swing.JLabel titoliLabel;
    private javax.swing.JLabel titoliLabel1;
    private javax.swing.JTextField titoliScuderiaTextField;
    private javax.swing.JTextField titoliTextField;
    private javax.swing.JPanel verificaPanel;
    // End of variables declaration//GEN-END:variables
}
