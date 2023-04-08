import jdk.dynalink.DynamicLinker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.IOException;

public class Main extends JFrame implements ActionListener {
    public void actionPerformed(ActionEvent e) {
    }
    public void onShowing() {
    }

    JPanel panel = new JPanel();
    JPanel panel1 = new JPanel();
    JPanel panel2 = new JPanel();
    JPanel panel3 = new JPanel();
    JPanel panel4 = new JPanel();

    JLabel mLbl = new JLabel("신우진의 도서관리프로그램");

    JButton Serchbtn = new JButton("책 검색");
    JButton AddBookbtn = new JButton("책 등록");
    JButton Updatebtn = new JButton("수정하기");

    JButton DeleteButton = new JButton("책 삭제");

    JTextField txtSerch = new JTextField(100);


    JComboBox Select = new JComboBox();

    static String Header[] = {"책 번호","책 제목", "저자", "출판사"};
    static DefaultTableModel model = new DefaultTableModel(Header,0);

    static JTable Serchtable = new JTable(model);


    JScrollPane scrollPane = new JScrollPane(Serchtable);







    public Main() {
        super.setTitle("신우진의 도서관리프로그램");
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER,400,10));
        panel1.add(mLbl);
        mLbl.setFont(mLbl.getFont().deriveFont(60f));
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
        panel2.add(Select);
        Select.addItem("전체");
        Select.addItem("책 제목");
        Select.addItem("저자");
        Select.addItem("출판사");
        panel2.add(txtSerch);
        panel2.add(Serchbtn);
        Serchbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = txtSerch.getText();
                int columnIndex = 1;
                for (int row = 0; row < model.getRowCount(); row++) {
                    String value = (String) model.getValueAt(row, columnIndex);
                    if (value.contains(searchText)) {
                        Serchtable.getSelectionModel().setSelectionInterval(row, row);
                        Serchtable.scrollRectToVisible(Serchtable.getCellRect(row, 0, true));
                        break;
                    }
                }
            }
        });
        panel2.add(AddBookbtn);
        panel3.setLayout(new FlowLayout(FlowLayout.CENTER,100,100));
        panel3.add(Serchtable);
        panel3.add(Serchtable.getTableHeader());
        Serchtable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        Serchtable.setSize(1800,800);
        Serchtable.setAutoCreateRowSorter(true);
        panel3.add(new JScrollPane(Serchtable));
        scrollPane.setLayout(null);
        scrollPane.setSize(1800,800);
        scrollPane.setPreferredSize(new Dimension(1800,800));
        scrollPane.setBackground(Color.white);

        AddBookbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AddBook();
            }
        });
        panel4.add(Updatebtn);
        Updatebtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = Serchtable.getSelectedRow();

                if (row >= 0) {
                    String bookNumber = Serchtable.getValueAt(row, 0).toString();
                    String bookTitle = Serchtable.getValueAt(row, 1).toString();
                    String author = Serchtable.getValueAt(row, 2).toString();
                    String publisher = Serchtable.getValueAt(row, 3).toString();

                    // 업데이트 양식을 표시할 새 JFrame 만들기
                    JFrame updateFrame = new JFrame("Update Book Information");
                    updateFrame.setSize(400, 300);
                    updateFrame.setLocationRelativeTo(null);

                    // 업데이트 양식 구성 요소를 보관할 JPanel 만들기
                    JPanel updatePanel = new JPanel();
                    updatePanel.setLayout(new GridLayout(5, 2));

                    // 각 책 정보 필드에 대한 JLabels 및 JTextFields 추가
                    JLabel bookNumberLabel = new JLabel("책 번호:");
                    JTextField bookNumberField = new JTextField(bookNumber);
                    JLabel bookTitleLabel = new JLabel("첵 제목:");
                    JTextField bookTitleField = new JTextField(bookTitle);
                    JLabel authorLabel = new JLabel("저자:");
                    JTextField authorField = new JTextField(author);
                    JLabel publisherLabel = new JLabel("출판사:");
                    JTextField publisherField = new JTextField(publisher);

                    // 패널에 구성 요소 추가
                    updatePanel.add(bookNumberLabel);
                    updatePanel.add(bookNumberField);
                    updatePanel.add(bookTitleLabel);
                    updatePanel.add(bookTitleField);
                    updatePanel.add(authorLabel);
                    updatePanel.add(authorField);
                    updatePanel.add(publisherLabel);
                    updatePanel.add(publisherField);

                    // 업데이트를 저장하는 버튼 추가
                    JButton saveButton = new JButton("저장");
                    saveButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // 텍스트 필드에서 업데이트된 값 가져오기
                            String newBookNumber = bookNumberField.getText();
                            String newBookTitle = bookTitleField.getText();
                            String newAuthor = authorField.getText();
                            String newPublisher = publisherField.getText();

                            // 표의 값 업데이트
                            Serchtable.setValueAt(newBookNumber, row, 0);
                            Serchtable.setValueAt(newBookTitle, row, 1);
                            Serchtable.setValueAt(newAuthor, row, 2);
                            Serchtable.setValueAt(newPublisher, row, 3);

                            // txt파일 값 업데이트
                            try {
                                updateBookInformationFile(newBookNumber, newBookTitle, newAuthor, newPublisher, bookNumber);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                            // 업데이트 프레임 닫기
                            updateFrame.dispose();
                        }
                    });
                    updatePanel.add(saveButton);

                    // 프레임에 패널을 추가하고 표시합니다
                    updateFrame.add(updatePanel);
                    updateFrame.setVisible(true);
                }
            }
        });

        panel4.add(DeleteButton);
        DeleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = Serchtable.getSelectedRows();
                if (selectedRows.length > 0) {
                    // 선택한 행을 반복하여 테이블 모델에서 제거
                    for (int i = selectedRows.length - 1; i >= 0; i--) {
                        int row = selectedRows[i];
                        model.removeRow(row);
                    }
                    // 업데이트된 데이터를 파일에 저장
                    try {
                        FileWriter writer = new FileWriter("bookinform.txt");
                        for (int row = 0; row < model.getRowCount(); row++) {
                            for (int col = 0; col < model.getColumnCount(); col++) {
                                writer.write(model.getValueAt(row, col).toString() + "/");
                            }
                            writer.write("\n");
                        }
                        writer.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a row to delete.");
                }
            }
        });
        add(panel);
        panel.add(panel1);
        panel.add(panel2);
        panel.add(panel3);
        panel.add(panel4);


        setBounds(200,200,1600,900);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Serchtable.setFillsViewportHeight(true);



    }

    public static void booklist() throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("C:/Users/user/IdeaProjects/booklist/bookinform.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        DefaultTableModel model = (DefaultTableModel)Serchtable.getModel();
        int cnt = 0;
        String str = null;
        String[] arr = new String[100];
        while((str = reader.readLine()) != null) {
            arr[cnt] = str;
            cnt++;
        }
        for(int i=0;i< arr.length;i++) {
            if(arr[i] == null){
                break;
            }
            String arr2[] = arr[i].split("/");
            int n = arr2.length;
            String[] newArray = new String[n];
            System.arraycopy(arr2,0,newArray,0,n);
            model.addRow(newArray);
        }


    }


    public static void main (String[]args) throws IOException {
        booklist();



        new Main();

    }

    public static void updateBookInformationFile(String bookNumber, String bookTitle, String author, String publisher, String oldBookNumber) throws IOException {
        File file = new File("C:/Users/user/IdeaProjects/booklist/bookinform.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        StringBuilder content = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            String[] data = line.split("/");
            if (data.length > 0 && data[0].equals(oldBookNumber)) {
                content.append(bookNumber).append("/").append(bookTitle).append("/").append(author).append("/").append(publisher).append("\n");
            } else {
                content.append(line).append("\n");
            }
        }

        reader.close();

        FileWriter writer = new FileWriter(file);
        writer.write(content.toString());
        writer.close();
    }
}
