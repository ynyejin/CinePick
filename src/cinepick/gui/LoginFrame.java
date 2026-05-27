package cinepick.gui;

import cinepick.model.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private AppContext context;

    private JTextField idField;
    private JPasswordField passwordField;

    public LoginFrame(AppContext context) {
        this.context = context;

        setTitle("CinePick - 로그인");
        setSize(420, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel titleLabel = new JLabel("CinePick", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        titleLabel.setForeground(new Color(40, 45, 60));

        JLabel subtitleLabel = new JLabel("Java Movie Reservation System", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(100, 100, 100));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 12));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 25, 0));

        JLabel idLabel = new JLabel("아이디");
        JLabel passwordLabel = new JLabel("비밀번호");

        idField = new JTextField();
        passwordField = new JPasswordField();

        formPanel.add(idLabel);
        formPanel.add(idField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        JButton loginButton = createButton("로그인");
        JButton registerButton = createButton("회원가입");
        JButton adminButton = createButton("관리자 로그인");

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 8));
        buttonPanel.setOpaque(false);
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(adminButton);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        loginButton.addActionListener(e -> login(false));
        adminButton.addActionListener(e -> login(true));
        registerButton.addActionListener(e -> register());
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(new Color(70, 90, 160));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void login(boolean adminLogin) {
        String userId = idField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (userId.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 입력해주세요.");
            return;
        }

        User user = context.userManager.login(userId, password);

        if (user == null) {
            JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호가 올바르지 않습니다.");
            return;
        }

        if (adminLogin && !user.getRole().equals("ADMIN")) {
            JOptionPane.showMessageDialog(this, "관리자 계정이 아닙니다.");
            return;
        }

        if (!adminLogin && !user.getRole().equals("USER")) {
            JOptionPane.showMessageDialog(this, "사용자 계정이 아닙니다.");
            return;
        }

        JOptionPane.showMessageDialog(this, user.getName() + "님, 로그인되었습니다.");

        if (adminLogin) {
            new AdminFrame(context, user).setVisible(true);
        } else {
            new UserMainFrame(context, user).setVisible(true);
        }

        dispose();
    }

    private void register() {
        String userId = JOptionPane.showInputDialog(this, "아이디를 입력하세요.");
        if (userId == null || userId.trim().isEmpty()) return;

        String password = JOptionPane.showInputDialog(this, "비밀번호를 입력하세요.");
        if (password == null || password.trim().isEmpty()) return;

        String name = JOptionPane.showInputDialog(this, "이름을 입력하세요.");
        if (name == null || name.trim().isEmpty()) return;

        boolean result = context.userManager.registerUser(userId.trim(), password.trim(), name.trim());

        if (result) {
            context.saveAll();
            JOptionPane.showMessageDialog(this, "회원가입이 완료되었습니다.");
        } else {
            JOptionPane.showMessageDialog(this, "이미 사용 중인 아이디입니다.");
        }
    }
}