/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package calculator;

import static calculator.Calculator.sInterface;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

public class Interface extends JFrame {

    public class AL extends KeyAdapter {
        public void keyPressed(KeyEvent e){
            
            int keyCode = e.getKeyCode();
            if(keyCode == e.VK_NUMPAD1){
                String tempNumber = displayTextField.getText() + jButton1.getText();
                displayTextField.setText(tempNumber);
            }
            if(keyCode == e.VK_NUMPAD2){
                String tempNumber = displayTextField.getText() + jButton2.getText();
                displayTextField.setText(tempNumber);
            }
            if(keyCode == e.VK_NUMPAD3){
                String tempNumber = displayTextField.getText() + jButton3.getText();
                displayTextField.setText(tempNumber);
            }
            if(keyCode == e.VK_NUMPAD4){
                String tempNumber = displayTextField.getText() + jButton4.getText();
                displayTextField.setText(tempNumber);
            }
            if(keyCode == e.VK_NUMPAD5){
                String tempNumber = displayTextField.getText() + jButton5.getText();
                displayTextField.setText(tempNumber);
            }
            if(keyCode == e.VK_NUMPAD6){
                String tempNumber = displayTextField.getText() + jButton6.getText();
                displayTextField.setText(tempNumber);
            }
            if(keyCode == e.VK_NUMPAD7){
                String tempNumber = displayTextField.getText() + jButton7.getText();
                displayTextField.setText(tempNumber);
            }
            if(keyCode == e.VK_NUMPAD8){
                String tempNumber = displayTextField.getText() + jButton8.getText();
                displayTextField.setText(tempNumber);
            }
            if(keyCode == e.VK_NUMPAD9){
                String tempNumber = displayTextField.getText() + jButton9.getText();
                displayTextField.setText(tempNumber);
            }
            if(keyCode == e.VK_NUMPAD0){
                String tempNumber = displayTextField.getText() + jButton0.getText();
                displayTextField.setText(tempNumber);
            }
            if(keyCode == e.VK_DECIMAL){
                if(!displayTextField.getText().contains("."))
                {
                    displayTextField.setText(displayTextField.getText() + jButtonDecimal.getText());
                }
            }
            
            
            if(keyCode == e.VK_MULTIPLY){
                firstN = Double.parseDouble(String.valueOf(displayTextField.getText()));
                displayTextField.setText("");
                operation = "*";
            }
            if(keyCode == e.VK_ADD){
                firstN = Double.parseDouble(String.valueOf(displayTextField.getText()));
                displayTextField.setText("");
                operation = "+";
            }
            if(keyCode == e.VK_SUBTRACT){
                firstN = Double.parseDouble(String.valueOf(displayTextField.getText()));
                displayTextField.setText("");
                operation = "-";
            }
            if(keyCode == e.VK_DIVIDE){
                firstN = Double.parseDouble(String.valueOf(displayTextField.getText()));
                displayTextField.setText("");
                operation = "/";
            }
            if(keyCode == e.VK_ENTER){
                secondN = Double.parseDouble(String.valueOf(displayTextField.getText()));
        
                if(operation.contains("+"))
                {
                    displayTextField.setText(Double.toString(firstN+secondN));
                }
                else if(operation.contains("-"))
                {
                    displayTextField.setText(Double.toString(firstN-secondN));
                }
                else if(operation.contains("*"))
                {
                    displayTextField.setText(Double.toString(firstN*secondN));
                }
                else if(operation.contains("/"))
                {
                    displayTextField.setText(Double.toString(firstN/secondN));
                }
            }
        }
    }
    
    double memory = 0;
    double firstN = 0;
    double secondN = 0;
    String operation = "";
    boolean advancedOn = false;
    
    public Interface() {
        initComponents();
        //I used displayTextField because this was "focused" on along with the buttons
        //the screen was selected so this was active. There are like different
        //layers to this.
        displayTextField.addKeyListener(new AL());
    }

    public void delete()
    {
        String temp = null;
        if(displayTextField.getText().length() > 0)
        {
            StringBuilder sb = new StringBuilder(displayTextField.getText());
            sb.deleteCharAt(displayTextField.getText().length() - 1);
            temp = sb.toString();
            displayTextField.setText(temp);
        }        
    }
    
    public void setupSize()
    {
        advancedOn = false;
        this.setSize(318,430);
    }
    
    private boolean emptyText()
    {
        return displayTextField.getText().length()==0 //if text field is empty
                ||(displayTextField.getText().charAt((displayTextField.getText().length() -1)) == '.'
                   && displayTextField.getText().charAt((0)) == '.');
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton0 = new javax.swing.JButton();
        jButtonDecimal = new javax.swing.JButton();
        displayTextField = new javax.swing.JTextField();
        jButtonPlusd = new javax.swing.JButton();
        jButtonMinus = new javax.swing.JButton();
        jButtonTimes = new javax.swing.JButton();
        jButtonDivide = new javax.swing.JButton();
        jButtonEquals = new javax.swing.JButton();
        jButtonFlip = new javax.swing.JButton();
        jButtonPercent = new javax.swing.JButton();
        jButtonSqrt = new javax.swing.JButton();
        jButtonNeg = new javax.swing.JButton();
        jButtonClear = new javax.swing.JButton();
        jButtonBackSpace = new javax.swing.JButton();
        jButtonMemoryClear = new javax.swing.JButton();
        jButtonMemoryRecall = new javax.swing.JButton();
        jButtonMemoryAdd = new javax.swing.JButton();
        jButtonMemorySub = new javax.swing.JButton();
        jButtonAdvanced = new javax.swing.JButton();
        jButtonMemorySave = new javax.swing.JButton();
        jButtonClearEntry = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("1");
        jButton1.setMaximumSize(new java.awt.Dimension(40, 40));
        jButton1.setMinimumSize(new java.awt.Dimension(40, 40));
        jButton1.setPreferredSize(new java.awt.Dimension(40, 40));
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        jButton2.setText("2");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });

        jButton3.setText("3");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });

        jButton4.setText("4");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });

        jButton5.setText("5");
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton5MouseClicked(evt);
            }
        });

        jButton6.setText("6");
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton6MouseClicked(evt);
            }
        });

        jButton7.setText("7");
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton7MouseClicked(evt);
            }
        });

        jButton8.setText("8");
        jButton8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton8MouseClicked(evt);
            }
        });

        jButton9.setText("9");
        jButton9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton9MouseClicked(evt);
            }
        });

        jButton0.setText("0");
        jButton0.setToolTipText("");
        jButton0.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton0MouseClicked(evt);
            }
        });

        jButtonDecimal.setText(".");
        jButtonDecimal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonDecimalMouseClicked(evt);
            }
        });

        displayTextField.setEditable(false);
        displayTextField.setBackground(new java.awt.Color(255, 255, 255));
        displayTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        displayTextField.setAutoscrolls(false);

        jButtonPlusd.setText("+");
        jButtonPlusd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonPlusdMouseClicked(evt);
            }
        });

        jButtonMinus.setText("-");
        jButtonMinus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonMinusMouseClicked(evt);
            }
        });

        jButtonTimes.setText("*");
        jButtonTimes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonTimesMouseClicked(evt);
            }
        });

        jButtonDivide.setText("/");
        jButtonDivide.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonDivideMouseClicked(evt);
            }
        });

        jButtonEquals.setText("=");
        jButtonEquals.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonEqualsMouseClicked(evt);
            }
        });

        jButtonFlip.setText("1/x");
        jButtonFlip.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonFlipMouseClicked(evt);
            }
        });

        jButtonPercent.setText("%");
        jButtonPercent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonPercentMouseClicked(evt);
            }
        });

        jButtonSqrt.setText("√");
        jButtonSqrt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonSqrtMouseClicked(evt);
            }
        });

        jButtonNeg.setText("±");
        jButtonNeg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonNegMouseClicked(evt);
            }
        });

        jButtonClear.setText("C");
        jButtonClear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonClearMouseClicked(evt);
            }
        });

        jButtonBackSpace.setText("←");
        jButtonBackSpace.setToolTipText("");
        jButtonBackSpace.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonBackSpaceMouseClicked(evt);
            }
        });

        jButtonMemoryClear.setText("MC");
        jButtonMemoryClear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonMemoryClearMouseClicked(evt);
            }
        });

        jButtonMemoryRecall.setText("MR");
        jButtonMemoryRecall.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonMemoryRecallMouseClicked(evt);
            }
        });

        jButtonMemoryAdd.setText("M+");
        jButtonMemoryAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonMemoryAddMouseClicked(evt);
            }
        });

        jButtonMemorySub.setText("M-");
        jButtonMemorySub.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonMemorySubMouseClicked(evt);
            }
        });

        jButtonAdvanced.setText("Advanced");
        jButtonAdvanced.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonAdvancedMouseClicked(evt);
            }
        });

        jButtonMemorySave.setText("MS");
        jButtonMemorySave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonMemorySaveMouseClicked(evt);
            }
        });

        jButtonClearEntry.setText("CE");
        jButtonClearEntry.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonClearEntryMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addComponent(jButtonAdvanced))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton0, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonDecimal, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButtonClear, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButtonClearEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButtonBackSpace, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                                    .addComponent(jButtonNeg, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButtonMinus, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonTimes, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonDivide, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonEquals, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonPlusd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(displayTextField))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButtonSqrt, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonMemorySave, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButtonMemoryClear, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonMemoryRecall, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButtonPercent, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonMemoryAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButtonFlip, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonMemorySub, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(displayTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonAdvanced)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(229, 229, 229)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonDecimal, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton0, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonDivide, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonBackSpace, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonClear, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonClearEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonTimes, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonMemoryClear, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonMemoryRecall, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButtonMinus, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButtonSqrt, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButtonMemorySave, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonPlusd, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonPercent, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonMemoryAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonEquals, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonNeg, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(227, 227, 227)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonMemorySub, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonFlip, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jButton1.getAccessibleContext().setAccessibleName("");
        jButton2.getAccessibleContext().setAccessibleName("");
        jButton3.getAccessibleContext().setAccessibleName("");
        jButton4.getAccessibleContext().setAccessibleName("");
        jButton5.getAccessibleContext().setAccessibleName("");
        jButton6.getAccessibleContext().setAccessibleName("");
        jButton7.getAccessibleContext().setAccessibleName("");
        jButton8.getAccessibleContext().setAccessibleName("");
        jButton9.getAccessibleContext().setAccessibleName("");
        jButton0.getAccessibleContext().setAccessibleName("");
        jButtonDecimal.getAccessibleContext().setAccessibleName("");
        jButtonPlusd.getAccessibleContext().setAccessibleName("");
        jButtonMinus.getAccessibleContext().setAccessibleName("");
        jButtonTimes.getAccessibleContext().setAccessibleName("");
        jButtonEquals.getAccessibleContext().setAccessibleName("");
        jButtonFlip.getAccessibleContext().setAccessibleName("");
        jButtonPercent.getAccessibleContext().setAccessibleName("");
        jButtonSqrt.getAccessibleContext().setAccessibleName("");
        jButtonNeg.getAccessibleContext().setAccessibleName("");
        jButtonMemoryClear.getAccessibleContext().setAccessibleName("");
        jButtonMemoryRecall.getAccessibleContext().setAccessibleName("");
        jButtonMemoryAdd.getAccessibleContext().setAccessibleName("");
        jButtonMemorySub.getAccessibleContext().setAccessibleName("");
        jButtonMemorySave.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonMemorySaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonMemorySaveMouseClicked
        memory = Double.parseDouble(String.valueOf(displayTextField.getText()));
    }//GEN-LAST:event_jButtonMemorySaveMouseClicked

    private void jButtonAdvancedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonAdvancedMouseClicked
        if(advancedOn)
        {
            advancedOn = false;
            this.setSize(318,430);                  //make the start off small. 
            displayTextField.setSize(300, 40);      //need to adjust text field with screen size
        }else
        {
            advancedOn = true;
            this.setSize(460,430);
            displayTextField.setSize(420, 40);      //need to adjust text field with screen size
        }
    }//GEN-LAST:event_jButtonAdvancedMouseClicked

    private void jButtonMemorySubMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonMemorySubMouseClicked
        memory -= Double.parseDouble(String.valueOf(displayTextField.getText()));
    }//GEN-LAST:event_jButtonMemorySubMouseClicked

    private void jButtonMemoryAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonMemoryAddMouseClicked
        memory += Double.parseDouble(String.valueOf(displayTextField.getText()));
    }//GEN-LAST:event_jButtonMemoryAddMouseClicked

    private void jButtonMemoryRecallMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonMemoryRecallMouseClicked
        displayTextField.setText(String.valueOf(memory));
    }//GEN-LAST:event_jButtonMemoryRecallMouseClicked

    private void jButtonMemoryClearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonMemoryClearMouseClicked
        memory = 0;
    }//GEN-LAST:event_jButtonMemoryClearMouseClicked

    private void jButtonBackSpaceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonBackSpaceMouseClicked
        delete();
    }//GEN-LAST:event_jButtonBackSpaceMouseClicked

    private void jButtonClearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonClearMouseClicked
        displayTextField.setText("");
        firstN = 0;
        secondN = 0;
    }//GEN-LAST:event_jButtonClearMouseClicked

    private void jButtonNegMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonNegMouseClicked

        if( emptyText())
        {
            //do nothing when text field is empty or only contains a decimal 
        }
        else if(displayTextField.getText().charAt((displayTextField.getText().length() -1)) == '.')
        {
            double temp = Double.parseDouble(String.valueOf(displayTextField.getText()));
            temp = temp *(-1);
            displayTextField.setText(String.valueOf(temp));
            delete();

        }
        else if(displayTextField.getText().contains("."))
        {
            double temp = Double.parseDouble(String.valueOf(displayTextField.getText()));
            temp = temp *(-1);
            displayTextField.setText(String.valueOf(temp));
        }
        else
        {
            double temp = Double.parseDouble(String.valueOf(displayTextField.getText()));
            temp = temp *(-1);
            displayTextField.setText(String.valueOf(temp));
            delete();
            delete();

        }        
    }//GEN-LAST:event_jButtonNegMouseClicked

    private void jButtonSqrtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSqrtMouseClicked
        double temp = Double.parseDouble(String.valueOf(displayTextField.getText()));
        temp = Math.sqrt(temp);
        displayTextField.setText(String.valueOf(temp));
    }//GEN-LAST:event_jButtonSqrtMouseClicked

    private void jButtonPercentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonPercentMouseClicked
        Double temp = Double.parseDouble(String.valueOf(displayTextField.getText()));
        displayTextField.setText(String.valueOf(firstN * (temp * .01)));
    }//GEN-LAST:event_jButtonPercentMouseClicked

    private void jButtonFlipMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonFlipMouseClicked
        double temp = Double.parseDouble(String.valueOf(displayTextField.getText()));
        temp = 1 / temp;
        displayTextField.setText(String.valueOf(temp));
    }//GEN-LAST:event_jButtonFlipMouseClicked

    private void jButtonEqualsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonEqualsMouseClicked
        secondN = Double.parseDouble(String.valueOf(displayTextField.getText()));
        
        if(operation.contains("+"))
        {
            displayTextField.setText(Double.toString(firstN+secondN));
        }
        else if(operation.contains("-"))
        {
            displayTextField.setText(Double.toString(firstN-secondN));
        }
        else if(operation.contains("*"))
        {
            displayTextField.setText(Double.toString(firstN*secondN));
        }
        else if(operation.contains("/"))
        {
            displayTextField.setText(Double.toString(firstN/secondN));
        }
    }//GEN-LAST:event_jButtonEqualsMouseClicked

    private void jButtonDivideMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonDivideMouseClicked
        firstN = Double.parseDouble(String.valueOf(displayTextField.getText()));
        displayTextField.setText("");
        operation = "/";
    }//GEN-LAST:event_jButtonDivideMouseClicked

    private void jButtonTimesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonTimesMouseClicked
        firstN = Double.parseDouble(String.valueOf(displayTextField.getText()));
        displayTextField.setText("");
        operation = "*";
    }//GEN-LAST:event_jButtonTimesMouseClicked

    private void jButtonMinusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonMinusMouseClicked
        firstN = Double.parseDouble(String.valueOf(displayTextField.getText()));
        displayTextField.setText("");
        operation = "-";
    }//GEN-LAST:event_jButtonMinusMouseClicked

    private void jButtonPlusdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonPlusdMouseClicked
        firstN = Double.parseDouble(String.valueOf(displayTextField.getText()));
        displayTextField.setText("");
        operation = "+";
    }//GEN-LAST:event_jButtonPlusdMouseClicked

    private void jButtonDecimalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonDecimalMouseClicked
        if(!displayTextField.getText().contains("."))
        {
            displayTextField.setText(displayTextField.getText() + jButtonDecimal.getText());
        }
    }//GEN-LAST:event_jButtonDecimalMouseClicked

    private void jButton0MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton0MouseClicked
        double tempZero = Double.parseDouble(String.valueOf(displayTextField.getText()+0));
        
        if(tempZero != 0 || displayTextField.getText().contains("."))
        {
        String tempNumber = displayTextField.getText() + jButton0.getText();
        displayTextField.setText(tempNumber);
        }

    }//GEN-LAST:event_jButton0MouseClicked

    private void jButton9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseClicked
        String tempNumber = displayTextField.getText() + jButton9.getText();
        displayTextField.setText(tempNumber);
    }//GEN-LAST:event_jButton9MouseClicked

    private void jButton8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseClicked
        String tempNumber = displayTextField.getText() + jButton8.getText();
        displayTextField.setText(tempNumber);
    }//GEN-LAST:event_jButton8MouseClicked

    private void jButton7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseClicked
        String tempNumber = displayTextField.getText() + jButton7.getText();
        displayTextField.setText(tempNumber);
    }//GEN-LAST:event_jButton7MouseClicked

    private void jButton6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseClicked
        String tempNumber = displayTextField.getText() + jButton6.getText();
        displayTextField.setText(tempNumber);
    }//GEN-LAST:event_jButton6MouseClicked

    private void jButton5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseClicked
        String tempNumber = displayTextField.getText() + jButton5.getText();
        displayTextField.setText(tempNumber);
    }//GEN-LAST:event_jButton5MouseClicked

    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseClicked
        String tempNumber = displayTextField.getText() + jButton4.getText();
        displayTextField.setText(tempNumber);
    }//GEN-LAST:event_jButton4MouseClicked

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        String tempNumber = displayTextField.getText() + jButton3.getText();
        displayTextField.setText(tempNumber);
    }//GEN-LAST:event_jButton3MouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        String tempNumber = displayTextField.getText() + jButton2.getText();
        displayTextField.setText(tempNumber);
    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        String tempNumber = displayTextField.getText() + jButton1.getText();
        displayTextField.setText(tempNumber);
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButtonClearEntryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonClearEntryMouseClicked
        displayTextField.setText("");
    }//GEN-LAST:event_jButtonClearEntryMouseClicked

    
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
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and disdisplayTextFieldrm */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interface().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField displayTextField;
    private javax.swing.JButton jButton0;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonAdvanced;
    private javax.swing.JButton jButtonBackSpace;
    private javax.swing.JButton jButtonClear;
    private javax.swing.JButton jButtonClearEntry;
    private javax.swing.JButton jButtonDecimal;
    private javax.swing.JButton jButtonDivide;
    private javax.swing.JButton jButtonEquals;
    private javax.swing.JButton jButtonFlip;
    private javax.swing.JButton jButtonMemoryAdd;
    private javax.swing.JButton jButtonMemoryClear;
    private javax.swing.JButton jButtonMemoryRecall;
    private javax.swing.JButton jButtonMemorySave;
    private javax.swing.JButton jButtonMemorySub;
    private javax.swing.JButton jButtonMinus;
    private javax.swing.JButton jButtonNeg;
    private javax.swing.JButton jButtonPercent;
    private javax.swing.JButton jButtonPlusd;
    private javax.swing.JButton jButtonSqrt;
    private javax.swing.JButton jButtonTimes;
    // End of variables declaration//GEN-END:variables
}
