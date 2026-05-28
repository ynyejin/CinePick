package cinepick.gui;

import cinepick.model.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private AppContext context;

    private JTextField idField;
    private JPasswordField passwordField;

    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color PRIMARY_COLOR = new Color(70, 90, 160);
    private static final Color TEXT_COLOR = new Color(40, 45, 60);
    private static final Color SUB_TEXT_COLOR = new Color(100, 100, 110);

    public LoginFrame(AppContext context) {
        this.context = context;

        setTitle("CinePick - 로그인");
        setSize(520, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel rootPanel = new JPanel(new GridBagLayout());
        rootPanel.setBackground(BACKGROUND_COLOR);
        rootPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel loginCard = new JPanel();
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setBackground(Color.WHITE);
        loginCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 235)),
                BorderFactory.createEmptyBorder(36, 42, 36, 42)
        ));
        loginCard.setPreferredSize(new Dimension(390, 430));

        JLabel logoLabel = new JLabel("CinePick");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 34));
        logoLabel.setForeground(TEXT_COLOR);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Java Movie Reservation System");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitleLabel.setForeground(SUB_TEXT_COLOR);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel loginTitleLabel = new JLabel("로그인");
        loginTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        loginTitleLabel.setForeground(TEXT_COLOR);
        loginTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel idLabel = createInputLabel("아이디");
        idField = createTextField();

        JLabel passwordLabel = createInputLabel("비밀번호");
        passwordField = createPasswordField();

        JButton loginButton = createPrimaryButton("사용자 로그인");
        JButton adminButton = createSecondaryButton("관리자 로그인");
        JButton registerButton = createOutlineButton("회원가입");

        loginCard.add(logoLabel);
        loginCard.add(Box.createVerticalStrut(6));
        loginCard.add(subtitleLabel);
        loginCard.add(Box.createVerticalStrut(32));

        loginCard.add(loginTitleLabel);
        loginCard.add(Box.createVerticalStrut(20));

        loginCard.add(idLabel);
        loginCard.add(Box.createVerticalStrut(6));
        loginCard.add(idField);
        loginCard.add(Box.createVerticalStrut(16));

        loginCard.add(passwordLabel);
        loginCard.add(Box.createVerticalStrut(6));
        loginCard.add(passwordField);
        loginCard.add(Box.createVerticalStrut(26));

        loginCard.add(loginButton);
        loginCard.add(Box.createVerticalStrut(10));
        loginCard.add(adminButton);
        loginCard.add(Box.createVerticalStrut(10));
        loginCard.add(registerButton);

        rootPanel.add(loginCard);
        add(rootPanel);

        loginButton.addActionListener(e -> login(false));
        adminButton.addActionListener(e -> login(true));
        registerButton.addActionListener(e -> register());
    }

    private JLabel createInputLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(TEXT_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 225)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 225)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(new Color(95, 110, 150));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return button;
    }

    private JButton createOutlineButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(Color.WHITE);
        button.setForeground(PRIMARY_COLOR);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
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

        String role = user.getRole().trim();

        if (adminLogin) {
            if (!role.equalsIgnoreCase("ADMIN")) {
                JOptionPane.showMessageDialog(this, "관리자 계정이 아닙니다.");
                return;
            }

            JOptionPane.showMessageDialog(this, "관리자 로그인 성공");
            new AdminFrame(context, user).setVisible(true);
            dispose();
            return;
        }

        if (!role.equalsIgnoreCase("USER")) {
            JOptionPane.showMessageDialog(this, "사용자 계정이 아닙니다.");
            return;
        }

        JOptionPane.showMessageDialog(this, user.getName() + "님, 로그인되었습니다.");
        new UserMainFrame(context, user).setVisible(true);
        dispose();
    }

    private void register() {
        JDialog dialog = new JDialog(this, "회원가입", true);
        dialog.setSize(440, 460);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JLabel titleLabel = new JLabel("회원가입");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_COLOR);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        JLabel idLabel = createInputLabel("아이디");
        JTextField registerIdField = createTextField();

        JButton checkButton = createSecondaryButton("아이디 중복 확인");
        JLabel checkResultLabel = new JLabel("아이디는 영문 소문자와 숫자 4~12자만 가능합니다.");
        checkResultLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        checkResultLabel.setForeground(SUB_TEXT_COLOR);

        JLabel passwordLabel = createInputLabel("비밀번호");
        JPasswordField registerPasswordField = createPasswordField();

        JLabel nameLabel = createInputLabel("이름");
        JTextField nameField = createTextField();

        final boolean[] checked = {false};
        final String[] checkedId = {""};

        gbc.gridy = 0;
        formPanel.add(idLabel, gbc);

        gbc.gridy = 1;
        formPanel.add(registerIdField, gbc);

        gbc.gridy = 2;
        formPanel.add(checkButton, gbc);

        gbc.gridy = 3;
        formPanel.add(checkResultLabel, gbc);

        gbc.gridy = 4;
        formPanel.add(passwordLabel, gbc);

        gbc.gridy = 5;
        formPanel.add(registerPasswordField, gbc);

        gbc.gridy = 6;
        formPanel.add(nameLabel, gbc);

        gbc.gridy = 7;
        formPanel.add(nameField, gbc);

        JButton registerButton = createPrimaryButton("가입하기");
        JButton cancelButton = createOutlineButton("취소");

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(panel);

        checkButton.addActionListener(e -> {
            String userId = registerIdField.getText().trim();

            if (userId.isEmpty()) {
                checkResultLabel.setText("아이디를 입력해주세요.");
                checkResultLabel.setForeground(new Color(190, 70, 70));
                checked[0] = false;
                return;
            }

            if (!userId.matches("^[a-z0-9]{4,12}$")) {
                checkResultLabel.setText("아이디는 영문 소문자와 숫자 4~12자만 가능합니다.");
                checkResultLabel.setForeground(new Color(190, 70, 70));
                checked[0] = false;
                return;
            }

            if (context.userManager.isDuplicateId(userId)) {
                checkResultLabel.setText("이미 사용 중인 아이디입니다.");
                checkResultLabel.setForeground(new Color(190, 70, 70));
                checked[0] = false;
            } else {
                checkResultLabel.setText("사용 가능한 아이디입니다.");
                checkResultLabel.setForeground(new Color(40, 130, 80));
                checked[0] = true;
                checkedId[0] = userId;
            }
        });

        registerIdField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                resetCheck();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                resetCheck();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                resetCheck();
            }

            private void resetCheck() {
                checked[0] = false;
                checkedId[0] = "";
                checkResultLabel.setText("아이디 중복 확인을 해주세요.");
                checkResultLabel.setForeground(SUB_TEXT_COLOR);
            }
        });

        registerButton.addActionListener(e -> {
            String userId = registerIdField.getText().trim();
            String password = new String(registerPasswordField.getPassword()).trim();
            String name = nameField.getText().trim();

            if (userId.isEmpty() || password.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "모든 항목을 입력해주세요.");
                return;
            }

            if (!userId.matches("^[a-z0-9]{4,12}$")) {
                JOptionPane.showMessageDialog(dialog, "아이디는 영문 소문자와 숫자 4~12자만 가능합니다.");
                return;
            }

            if (!checked[0] || !checkedId[0].equalsIgnoreCase(userId)) {
                JOptionPane.showMessageDialog(dialog, "아이디 중복 확인을 먼저 해주세요.");
                return;
            }

            boolean result = context.userManager.registerUser(userId, password, name);

            if (result) {
                context.saveAll();

                idField.setText(userId);
                passwordField.setText(password);

                JOptionPane.showMessageDialog(dialog, "회원가입이 완료되었습니다.");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "이미 사용 중인 아이디입니다.");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }
}