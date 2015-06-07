/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FrmUtama.java
 *
 * Dibuat pada tanggal 30 Mei 2015, 12:17:36
 */

package SimPenCPU;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author sofian
 */
public class FrmUtama extends javax.swing.JFrame {
    private String[] namakolom={"Nama Proses","Lama Proses","Saat Tiba"};
    private Object[][] data=null;
    private Object[][] simpan=null;
    private Object[][] datatersimpan=null;

    int row;
    int x;
    int jumlah=0;
    int sAWT,sTAT,sAWTSJF;
    float AWT,TAT;

    int[] A,B,C;

    FrmTAT h = new FrmTAT(this, true);
    FrmAWT a = new FrmAWT(this, true);

    
    /** Creates new form frmUtama */
    public FrmUtama() {
        initComponents();

        A = new int[10];
        B = new int[10];
        C = new int[10];

        setOnOff(false);
        bersih();
        setTombol(true);
        data = new Object[10][3];
    }

    public void setField(){
        this.row= jTablePoses.getSelectedRow();
        jTextFieldProses.setText((String)jTablePoses.getValueAt(row, 0));
        jTextFieldProsesCPU.setText((String)jTablePoses.getValueAt(row, 1));
        jTextFieldWaktuTiba.setText((String)jTablePoses.getValueAt(row, 2));
    }

    public void setTombol(boolean t){
        jButtonInput.setEnabled(t);
        jButtonHitungAWT.setEnabled(t);
        jButtonHitungTAT.setEnabled(t);
        jButtonSimpan.setEnabled(!t);
        jButtonBatal.setEnabled(!t);
    }

    public void bersih(){
        jTextFieldProses.setText("");
        jTextFieldProsesCPU.setText("");
        jTextFieldWaktuTiba.setText("");
    }
    public void setData(){
        
        data[x-1][0]=jTextFieldProses.getText();
        data[x-1][1]=jTextFieldProsesCPU.getText();
        data[x-1][2]=jTextFieldWaktuTiba.getText();

        jTablePoses.setModel(new DefaultTableModel(
                data,namakolom ));
    }


    public void setUlang(){
        this.dispose();
        FrmUtama q = new FrmUtama();
        q.setVisible(true);
    }

    public void setOnOff(boolean t){
        jTextFieldProses.setEnabled(t);
        jTextFieldProsesCPU.setEnabled(t);
        jTextFieldWaktuTiba.setEnabled(t);
    }

    //sorting berdasarkan waktu tiba
    public void sortingFIFO(){
        for(int i = 0 ; i < x-1 ; i++){
            for(int j = i+1 ; j<x ; j++){
                if(A[i]>A[j]){
                    int tampung;
                    tampung = A[i];
                    A[i] = A[j];
                    A[j] = tampung;
                    int tampungB;
                    tampungB = B[i];
                    B[i]=B[j];
                    B[j]=tampungB;
                }
            }
        }
    }

    //mengambil data dari tabel
    public void ambildata(){
        for(int a = 0 ; a<x ; a++){
            A[a] =Integer.parseInt((String)jTablePoses.getValueAt(a, 2));
            B[a] = Integer.parseInt((String)jTablePoses.getValueAt(a, 1));
        }
    }

    //sorting data untuk keperluan proses perhitungan fifo
    public void tatFIFO(){
        int[] jmlProsesTAT;
        jmlProsesTAT = new int[10];
        sortingFIFO();
        for(int m = 0 ; m<x;m++){
                jmlProsesTAT[m+1]=jmlProsesTAT[m]+B[m];
        }
        for(int o = 0 ; o<x ; o++){
            sTAT=sTAT+jmlProsesTAT[o+1];
        }
        TAT = (float)sTAT/x;

        h.setTAT(TAT);

        sTAT=0;
    }

    //sorting data untuk proses keperluan SJF
    public void sortingSJF(){
        for(int i = 0 ; i < x-1 ; i++){
            for(int j = i+1 ; j<x ; j++){
                if(B[i]>B[j]){
                    int tampungB;
                    tampungB = B[i];
                    B[i]=B[j];
                    B[j]=tampungB;
                    int tampungA;
                    tampungA = A[i];
                    A[i]=A[j];
                    A[j]=tampungB;
                }
            }
            
        }

        //untuk melakukan pengecekan sorting
        for(int q=0;q<x;q++){
            System.out.println(""+B[q]);
        }
    }

    //proses frmAWT untuk SJF
    public void tatSJF(){
        int[] jmlProsesTAT;
        jmlProsesTAT = new int[10];
        sortingSJF();
        for(int m = 0 ; m<x;m++){
                jmlProsesTAT[m+1]=jmlProsesTAT[m]+B[m];
        }
        for(int o = 0 ; o<x ; o++){
            sTAT=sTAT+jmlProsesTAT[o+1];
        }
        TAT = (float)sTAT/x;

        h.setTATSJF(TAT);

        sTAT=0;

    }

    public void awtFIFO(){
        int[] jmlProses;
        jmlProses = new int[10];
        ambildata();
        sortingFIFO(); //berdasarkan waktu tiba increase

        //menghitung jumlah proses sebelumnya dan kemudian di tampung sesuai urutannya
        C[0]=0;
        jmlProses[0]=0;
        for(int m = 0 ; m<x-1;m++){
                jmlProses[m+1]=jmlProses[m]+B[m];
        }

        //jml proses dikurangi waktu tiba, ditampung di C lalu di total di sAWT
        sAWT = 0;
        for(int k = 1 ; k<x; k++){
            C[k]=jmlProses[k]-A[k];
            sAWT = sAWT+C[k];
        }

        //di bagi sejumlah proses yg di input
        AWT=(float)sAWT/x;

        a.setAWT(AWT); //setnilai awt pada form awt    
    }

    public void awtSJF(){
        int[] jmlProses;
        jmlProses = new int[10];
        ambildata();
        sortingSJF(); //berdasarkan waktu tiba increase

        //menghitung jumlah proses sebelumnya dan kemudian di tampung sesuai urutannya
        
        jmlProses[0]=0;
        for(int m = 0 ; m<x;m++){
                jmlProses[m+1]=jmlProses[m]+B[m];
                System.out.println("jml proses"+jmlProses[m]); //check jmlProses
        }

        //jml proses dikurangi waktu tiba, ditampung di D lalu di total di sAWT
        sAWTSJF = 0;
        for(int k = 1 ; k<x; k++){
            sAWTSJF = sAWTSJF+jmlProses[k];
            System.out.println("sAWTSJF"+sAWTSJF);
        }

        //di bagi sejumlah proses yg di input
        AWT=(float)sAWTSJF/x;

        a.setAWTSJF(AWT); //setnilai awt pada form awt     

    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenuBar3 = new javax.swing.JMenuBar();
        jMenu5 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldProses = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldProsesCPU = new javax.swing.JTextField();
        jTextFieldWaktuTiba = new javax.swing.JTextField();
        jButtonInput = new javax.swing.JButton();
        jButtonUlangi = new javax.swing.JButton();
        jButtonSimpan = new javax.swing.JButton();
        jButtonBatal = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTablePoses = new javax.swing.JTable();
        jButtonHitungTAT = new javax.swing.JButton();
        jButtonHitungAWT = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jMenu2.setText("File");
        jMenuBar2.add(jMenu2);

        jMenu4.setText("Edit");
        jMenuBar2.add(jMenu4);

        jMenu5.setText("File");
        jMenuBar3.add(jMenu5);

        jMenu6.setText("Edit");
        jMenuBar3.add(jMenu6);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simulasi Penjadwalan Memori");
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(7, 138, 72));

        jLabel1.setFont(new java.awt.Font("Adventure", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Simulasi Penjadwalan Memori");

        jPanel2.setBackground(new java.awt.Color(7, 138, 72));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Informasi Proses", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Nama Proses");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Lama Proses");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Saat Tiba");

        jTextFieldWaktuTiba.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldWaktuTibaActionPerformed(evt);
            }
        });

        jButtonInput.setBackground(new java.awt.Color(212, 170, 0));
        jButtonInput.setForeground(new java.awt.Color(254, 254, 254));
        jButtonInput.setText("Input");
        jButtonInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInputActionPerformed(evt);
            }
        });

        jButtonUlangi.setBackground(new java.awt.Color(212, 170, 0));
        jButtonUlangi.setForeground(new java.awt.Color(254, 254, 254));
        jButtonUlangi.setText("Ulangi");
        jButtonUlangi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUlangiActionPerformed(evt);
            }
        });

        jButtonSimpan.setBackground(new java.awt.Color(212, 170, 0));
        jButtonSimpan.setForeground(new java.awt.Color(254, 254, 254));
        jButtonSimpan.setText("Simpan");
        jButtonSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSimpanActionPerformed(evt);
            }
        });

        jButtonBatal.setBackground(new java.awt.Color(212, 170, 0));
        jButtonBatal.setForeground(new java.awt.Color(254, 254, 254));
        jButtonBatal.setText("Batal");
        jButtonBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBatalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButtonInput)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonUlangi)
                        .addGap(6, 6, 6)
                        .addComponent(jButtonSimpan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonBatal))
                    .addComponent(jTextFieldProses)
                    .addComponent(jTextFieldProsesCPU)
                    .addComponent(jTextFieldWaktuTiba))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldProses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldProsesCPU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldWaktuTiba, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonInput)
                    .addComponent(jButtonUlangi)
                    .addComponent(jButtonSimpan)
                    .addComponent(jButtonBatal))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(7, 138, 72));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Hasil Pemrosesan", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jTablePoses.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTablePoses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Nama Proses", "Lama Proses", "Saat Tiba"
            }
        ));
        jTablePoses.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablePosesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTablePoses);

        jButtonHitungTAT.setBackground(new java.awt.Color(212, 170, 0));
        jButtonHitungTAT.setForeground(new java.awt.Color(254, 254, 254));
        jButtonHitungTAT.setText("Hitung TAT");
        jButtonHitungTAT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHitungTATActionPerformed(evt);
            }
        });

        jButtonHitungAWT.setBackground(new java.awt.Color(212, 170, 0));
        jButtonHitungAWT.setForeground(new java.awt.Color(254, 254, 254));
        jButtonHitungAWT.setText("Hitung AWT");
        jButtonHitungAWT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHitungAWTActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 734, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jButtonHitungTAT)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonHitungAWT)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonHitungAWT)
                    .addComponent(jButtonHitungTAT))
                .addContainerGap())
        );

        jLabel6.setFont(new java.awt.Font("Adventure", 0, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Simulasi Penjadwalan CPU");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(174, 174, 174)
                                .addComponent(jLabel6)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1685, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(137, 137, 137))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(82, Short.MAX_VALUE))
        );

        jMenu1.setText("Berkas");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem1.setText("Keluar");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Bantuan");
        jMenu3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu3ActionPerformed(evt);
            }
        });

        jMenuItem5.setText("Tentang Program");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(801, 472));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInputActionPerformed
        // TODO add your handling code here:
        bersih();
        setOnOff(true);
        this.x=this.x+1;
        setTombol(false);
    }//GEN-LAST:event_jButtonInputActionPerformed

    private void jButtonBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBatalActionPerformed
        // TODO add your handling code here:
        setOnOff(false);
        setTombol(true);
        this.x=this.x-1;
}//GEN-LAST:event_jButtonBatalActionPerformed

    private void jButtonSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSimpanActionPerformed
        // TODO add your handling code here:
        setData();
        setTombol(true);
        bersih();
        setOnOff(false);
}//GEN-LAST:event_jButtonSimpanActionPerformed

    private void jButtonHitungAWTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHitungAWTActionPerformed
        // TODO add your handling code here:

        //FIFO dan SJF
        awtFIFO();
        awtSJF();

        //menampilkan form frmAWT
        a.setVisible(true);

    }//GEN-LAST:event_jButtonHitungAWTActionPerformed

    private void jButtonHitungTATActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHitungTATActionPerformed
        // TODO add your handling code here:
        ambildata();
        //FIFO
        tatFIFO();
        //SJF
        tatSJF();
        //menampilkan form
        h.setVisible(true);
    }//GEN-LAST:event_jButtonHitungTATActionPerformed

    private void jTablePosesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablePosesMouseClicked
        // TODO add your handling code here:
        setField();
    }//GEN-LAST:event_jTablePosesMouseClicked

    private void jButtonUlangiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUlangiActionPerformed
        // TODO add your handling code here:
        setUlang();
    }//GEN-LAST:event_jButtonUlangiActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jTextFieldWaktuTibaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWaktuTibaActionPerformed
        // TODO add your handling code here:
        setData();
        setTombol(true);
        bersih();
        setOnOff(false);
    }//GEN-LAST:event_jTextFieldWaktuTibaActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
        FrmTentang tentang = new FrmTentang(this, true);
        tentang.setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenu3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu3ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmUtama().setVisible(true);
               
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBatal;
    private javax.swing.JButton jButtonHitungAWT;
    private javax.swing.JButton jButtonHitungTAT;
    private javax.swing.JButton jButtonInput;
    private javax.swing.JButton jButtonSimpan;
    private javax.swing.JButton jButtonUlangi;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuBar jMenuBar3;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTablePoses;
    private javax.swing.JTextField jTextFieldProses;
    private javax.swing.JTextField jTextFieldProsesCPU;
    private javax.swing.JTextField jTextFieldWaktuTiba;
    // End of variables declaration//GEN-END:variables

}

