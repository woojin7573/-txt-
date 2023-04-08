import com.sun.tools.javac.Main;

import java.awt.event.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;

public class AddBook extends JFrame implements ActionListener{

    JPanel panel;
    JLabel Booknumber, Bookname, Bookwriter, Publisher;
    JButton b1, b2;
    JTextField txtBooknumber, txtBookname, txtBookwriter, txtPublisher;


    AddBook(){
        setTitle("책 등록하기");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel= new JPanel();
        panel.setLayout(new GridLayout(0, 2));

        Booknumber = new JLabel("책번호");
        Bookname = new JLabel("책 이름");
        Bookwriter = new JLabel("저자");
        Publisher = new JLabel("출판사");

        txtBooknumber = new JTextField(20);
        txtBookname = new JTextField(20);
        txtBookwriter = new JTextField(20);
        txtPublisher = new JTextField(20);

        b1 = new JButton("등록하기");
        b2 = new JButton("뒤로가기");

        panel.add(Booknumber);
        panel.add(txtBooknumber);
        panel.add(Bookname);
        panel.add(txtBookname);
        panel.add(Bookwriter);
        panel.add(txtBookwriter);
        panel.add(Publisher);
        panel.add(txtPublisher);
        panel.add(b1);
        panel.add(b2);

        add(panel);

        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Main();
            }
        });


        b1.addActionListener(this);
        b2.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            String s = null;
            boolean isOk = false;
            BufferedWriter bw = new BufferedWriter(new FileWriter("bookinform.txt", true));
            BufferedReader br = new BufferedReader(new FileReader("bookinform.txt"));

            if (e.getSource() == b1) {
                while ((s = br.readLine()) != null) {

                    // 도서 중복
                    String[] array = s.split("/");
                    if (array[0].equals(txtBooknumber.getText())) {
                        isOk = true;
                    } else {
                        txtBookwriter.getText();
                        break;
                    }
                }
                // 정보 입력시 중복이 없으면 데이터 보냄
                if (!isOk) {
                    bw.write(txtBooknumber.getText() + "/");
                    bw.write(txtBookname.getText() + "/");
                    bw.write(txtBookwriter.getText() + "/");
                    bw.write(txtPublisher.getText() + "\r\n");
                    bw.close();

                    JOptionPane.showMessageDialog(null, "책 등록이 완료되었습니다.");
                    dispose();
                    new Main();
                } else {
                    JOptionPane.showMessageDialog(null, "책 등록이 실패하였습니다.(책 번호 중복)");
                }

            } else if (e.getSource() == b2) {
                txtBooknumber.setText("");
                txtBookname.setText("");
                txtBookwriter.setText("");
                txtPublisher.setText("");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "등록실패");
        }
    }
}