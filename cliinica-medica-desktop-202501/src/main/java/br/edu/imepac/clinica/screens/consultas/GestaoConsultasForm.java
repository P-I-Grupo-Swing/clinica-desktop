/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.edu.imepac.clinica.screens.consultas;
import br.edu.imepac.clinica.daos.ConsultasDao;
import br.edu.imepac.clinica.daos.MedicoDao;
import br.edu.imepac.clinica.daos.PacienteDao;
import br.edu.imepac.clinica.daos.ConvenioDao;
import br.edu.imepac.clinica.entidades.Consulta;
import br.edu.imepac.clinica.entidades.Medico;
import br.edu.imepac.clinica.entidades.Paciente;
import br.edu.imepac.clinica.entidades.Convenio;
import br.edu.imepac.clinica.enums.StatusConsulta;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
/**
 *
 * @author Pedro Fernandes
 */
public class GestaoConsultasForm extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GestaoConsultasForm.class.getName());
 private ConsultasDao consultaDao = new ConsultasDao();
    private MedicoDao medicoDao = new MedicoDao();
    private PacienteDao pacienteDao = new PacienteDao();
    private ConvenioDao convenioDao = new ConvenioDao();

    // 2. Formatador de Data (Dia/Mes/Ano Hora:Minuto)
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    private Long idSelecionado = null;

    public GestaoConsultasForm() {
        initComponents();
        this.setLocationRelativeTo(null);
        
        configurarMascaraData(); // Arruma o campo de data
        carregarCombos();        // Preenche as caixinhas com dados do banco
        carregarTabela();        // Lista os agendamentos
    }
    /**
     * Creates new form GestaoConsultasForm
     */
   private void configurarMascaraData() {
        try {
            MaskFormatter mask = new MaskFormatter("##/##/#### ##:##");
            mask.setPlaceholderCharacter('_');
            mask.install(txtDataHora);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Busca dados no banco e preenche as caixas de seleção
    private void carregarCombos() {
        // Médicos
        cbMedico.setModel(new DefaultComboBoxModel(medicoDao.listarTodos().toArray()));
        
        // Pacientes
        cbPaciente.setModel(new DefaultComboBoxModel(pacienteDao.listarTodos().toArray()));
        
        // Convênios
        cbConvenio.setModel(new DefaultComboBoxModel(convenioDao.listarTodos().toArray()));
    }

    private void carregarTabela() {
        DefaultTableModel modelo = (DefaultTableModel) tblConsultas.getModel();
        modelo.setNumRows(0);
        
        List<Consulta> lista = consultaDao.listarTodos();
        
        for (Consulta c : lista) {
            // Formata a data para String
            String dataBonita = c.getDataHora().format(formatter);
            
            modelo.addRow(new Object[]{
                c.getId(),
                dataBonita,
                c.getMedico().getNome(),   // Mostra nome do médico
                c.getPaciente().getNome(), // Mostra nome do paciente
                c.getConvenio().getNomeEmpresa(),
                c.getStatus()
            });
        }
    }

    private void limparCampos() {
        txtDataHora.setValue(null);
        txtDataHora.setText("");
        txtObservacoes.setText("");
        
        // Reseta os combos para o primeiro item (se houver)
        if (cbMedico.getItemCount() > 0) cbMedico.setSelectedIndex(0);
        if (cbPaciente.getItemCount() > 0) cbPaciente.setSelectedIndex(0);
        if (cbConvenio.getItemCount() > 0) cbConvenio.setSelectedIndex(0);
        
        idSelecionado = null;
        tblConsultas.clearSelection();
        btnAgendar.setEnabled(true);
    }

    private Consulta montarObjeto() {
        // Validação da Data
        LocalDateTime dataHora = null;
        try {
            dataHora = LocalDateTime.parse(txtDataHora.getText(), formatter);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Data inválida! Use o formato: dd/MM/yyyy HH:mm");
            return null;
        }

        Consulta c = new Consulta();
        c.setDataHora(dataHora);
        c.setObservacoes(txtObservacoes.getText());
        
        // Pega os objetos inteiros selecionados nos combos
        c.setMedico((Medico) cbMedico.getSelectedItem());
        c.setPaciente((Paciente) cbPaciente.getSelectedItem());
        c.setConvenio((Convenio) cbConvenio.getSelectedItem());
        
        // Por padrão, nasce como AGENDADA (definido na classe Consulta)
        
        return c;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbPaciente = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        cbMedico = new javax.swing.JComboBox<>();
        cbConvenio = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtDataHora = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtObservacoes = new javax.swing.JTextArea();
        btnAgendar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblConsultas = new javax.swing.JTable();
        txtSair = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel1.setText("Agendamento de Consultas");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel2.setText("Paciente:");

        cbPaciente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel3.setText("Médico:");

        cbMedico.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbConvenio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel4.setText("Convênio:");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel5.setText("Data/Hora:");

        jLabel6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel6.setText("Observações:");

        txtObservacoes.setColumns(20);
        txtObservacoes.setRows(5);
        jScrollPane1.setViewportView(txtObservacoes);

        btnAgendar.setText("Agendar");
        btnAgendar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgendarActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnLimpar.setText("Limpar");
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        tblConsultas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Data/Hora", "Médico", "Paciente", "Convênio", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblConsultas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblConsultasMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblConsultas);

        txtSair.setText("Sair");
        txtSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSairActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(284, 284, 284)
                                .addComponent(jLabel1))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbConvenio, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbPaciente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbMedico, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtDataHora)))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btnAgendar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCancelar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnLimpar)))
                        .addGap(0, 442, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(txtSair)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cbPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbMedico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cbConvenio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtDataHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgendar)
                    .addComponent(btnCancelar)
                    .addComponent(btnLimpar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(txtSair)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgendarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgendarActionPerformed
Consulta c = montarObjeto();
    if (c != null) {
        // Validação extra: Médico e Paciente selecionados?
        if (c.getMedico() == null || c.getPaciente() == null) {
            JOptionPane.showMessageDialog(this, "Selecione Médico e Paciente!");
            return;
        }

        if (consultaDao.salvar(c)) {
            JOptionPane.showMessageDialog(this, "Agendamento realizado com sucesso!");
            limparCampos();
            carregarTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao agendar.");
        }
    }        // TODO add your handling code here:
    }//GEN-LAST:event_btnAgendarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
if (idSelecionado == null) {
        JOptionPane.showMessageDialog(this, "Selecione uma consulta na tabela para cancelar.");
        return;
    }
    
    if (JOptionPane.showConfirmDialog(this, "Deseja realmente cancelar esta consulta?", "Cancelar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
        
        // Buscamos a consulta original
        Consulta c = consultaDao.buscarPorId(idSelecionado);
        if (c != null) {
            c.setStatus(StatusConsulta.CANCELADA); // Muda o status
            
            if (consultaDao.atualizar(c)) {
                JOptionPane.showMessageDialog(this, "Consulta cancelada.");
                limparCampos();
                carregarTabela();
            }
        }
    }        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
limparCampos();        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimparActionPerformed

    private void txtSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSairActionPerformed
         this.dispose();  // TODO add your handling code here:
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSairActionPerformed

    private void tblConsultasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblConsultasMouseClicked
int linha = tblConsultas.getSelectedRow();
    
    if (linha != -1) {
        idSelecionado = Long.valueOf(tblConsultas.getValueAt(linha, 0).toString());
        
        txtDataHora.setText(tblConsultas.getValueAt(linha, 1).toString());
        
        btnAgendar.setEnabled(false);
        
      
    }        // TODO add your handling code here:
    }//GEN-LAST:event_tblConsultasMouseClicked

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new GestaoConsultasForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgendar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JComboBox<String> cbConvenio;
    private javax.swing.JComboBox<String> cbMedico;
    private javax.swing.JComboBox<String> cbPaciente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblConsultas;
    private javax.swing.JFormattedTextField txtDataHora;
    private javax.swing.JTextArea txtObservacoes;
    private javax.swing.JButton txtSair;
    // End of variables declaration//GEN-END:variables
}
