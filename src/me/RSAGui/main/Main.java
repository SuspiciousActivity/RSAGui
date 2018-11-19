package me.RSAGui.main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.math.BigInteger;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class Main extends JFrame {

	private static JPanel contentPane;
	private static JBigIntegerTextField txtN;
	private static JBigIntegerTextField txtP;
	private static JBigIntegerTextField txtQ;
	private static JBigIntegerTextField txtPhiN;
	private static JBigIntegerTextField txtD;
	private static JBigIntegerTextField txtE;
	private static JBigIntegerTextField txtC;
	private static JBigIntegerTextField txtM;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		setTitle("RSA Gui");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 265);
		setBounds((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - 225,
				(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - 132, 450, 265);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblN = new JLabel("n =");
		lblN.setForeground(Color.GREEN);
		lblN.setBounds(10, 11, 26, 14);
		contentPane.add(lblN);

		txtN = new JBigIntegerTextField();
		txtN.setToolTipText("n is the public modulus (p * q)");
		txtN.setBounds(34, 8, 400, 20);
		contentPane.add(txtN);
		txtN.setColumns(10);

		JLabel lblP = new JLabel("p =");
		lblP.setForeground(Color.GREEN);
		lblP.setBounds(10, 39, 26, 14);
		contentPane.add(lblP);

		txtP = new JBigIntegerTextField();
		txtP.setToolTipText("p is a prime, used to calculate n, d, phi");
		txtP.setColumns(10);
		txtP.setBounds(34, 36, 400, 20);
		contentPane.add(txtP);

		JLabel lblQ = new JLabel("q =");
		lblQ.setForeground(Color.GREEN);
		lblQ.setBounds(10, 67, 26, 14);
		contentPane.add(lblQ);

		txtQ = new JBigIntegerTextField();
		txtQ.setToolTipText("q is a prime, used to calculate n, d, phi");
		txtQ.setColumns(10);
		txtQ.setBounds(34, 64, 400, 20);
		contentPane.add(txtQ);

		JLabel lblD = new JLabel("d =");
		lblD.setForeground(Color.GREEN);
		lblD.setBounds(10, 151, 26, 14);
		contentPane.add(lblD);

		txtD = new JBigIntegerTextField();
		txtD.setToolTipText("d is the private decryption exponent");
		txtD.setColumns(10);
		txtD.setBounds(34, 148, 400, 20);
		contentPane.add(txtD);

		JLabel lblE = new JLabel("e =");
		lblE.setForeground(Color.GREEN);
		lblE.setBounds(10, 123, 26, 14);
		contentPane.add(lblE);

		txtE = new JBigIntegerTextField();
		txtE.setToolTipText("e is the public encryption exponent, usually 65537 or 3");
		txtE.setColumns(10);
		txtE.setBounds(34, 120, 400, 20);
		contentPane.add(txtE);

		JLabel lblC = new JLabel("c =");
		lblC.setForeground(Color.GREEN);
		lblC.setBounds(10, 208, 26, 14);
		contentPane.add(lblC);

		txtC = new JBigIntegerTextField();
		txtC.setToolTipText("c is the cipher of m");
		txtC.setColumns(10);
		txtC.setBounds(34, 205, 400, 20);
		contentPane.add(txtC);

		JLabel lblM = new JLabel("m =");
		lblM.setForeground(Color.GREEN);
		lblM.setBounds(10, 179, 26, 14);
		contentPane.add(lblM);

		txtM = new JBigIntegerTextField();
		txtM.setToolTipText("m is a plaint text message");
		txtM.setColumns(10);
		txtM.setBounds(34, 176, 400, 20);
		contentPane.add(txtM);

		JLabel lblPhi = new JLabel("\u03BB =");
		lblPhi.setForeground(Color.GREEN);
		lblPhi.setBounds(10, 95, 26, 14);
		contentPane.add(lblPhi);

		txtPhiN = new JBigIntegerTextField();
		txtPhiN.setToolTipText("phi is defined as lcm(p - 1, q - 1), used to calculate d");
		txtPhiN.setColumns(10);
		txtPhiN.setBounds(34, 92, 400, 20);
		contentPane.add(txtPhiN);
	}

	public static void onTextEnter(JBigIntegerTextField txt) {
		switch (txt.getToolTipText().split(" ")[0]) {
		case "n": {
			BigInteger n = txtN.getNumber();
			if (n == null) {
				return;
			}
			BigInteger[] factors = Util.factorN(n);
			if (factors == null) {
				JOptionPane.showMessageDialog(contentPane, "FactorDB couldn't give us factors!", "No factors found!",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			txtP.setNumber(factors[0]);
			txtQ.setNumber(factors[1]);
			try {
				txtPhiN.setNumber(Util.getPhi(factors[0], factors[1]));
			} catch (ArithmeticException ex) {
				JOptionPane.showMessageDialog(contentPane, "Can not calculate phi:\n" + ex.getMessage(),
						"Invalid values!", JOptionPane.ERROR_MESSAGE);
			}
			// after changing m we have to be able to recalculate m and c
			txtM.filter.valueChanged = true;
			txtC.filter.valueChanged = true;
			// recalculate d
			onTextEnter(txtE);
			break;
		}
		case "p": {
			BigInteger p = txtP.getNumber();
			if (p == null) {
				return;
			}
			BigInteger q = txtQ.getNumber();
			if (q == null) {
				BigInteger n = txtN.getNumber();
				if (n == null) {
					return;
				} else {
					q = n.divide(p);
					txtQ.setNumber(q);
				}
			}
			txtN.setNumber(p.multiply(q));
			try {
				txtPhiN.setNumber(Util.getPhi(p, q));
			} catch (ArithmeticException ex) {
				JOptionPane.showMessageDialog(contentPane, "Can not calculate phi:\n" + ex.getMessage(),
						"Invalid values!", JOptionPane.ERROR_MESSAGE);
			}
			break;
		}
		case "q": {
			BigInteger q = txtQ.getNumber();
			if (q == null) {
				return;
			}
			BigInteger p = txtP.getNumber();
			if (p == null) {
				BigInteger n = txtN.getNumber();
				if (n == null) {
					return;
				} else {
					p = n.divide(q);
					txtP.setNumber(p);
				}
			}
			txtN.setNumber(p.multiply(q));
			try {
				txtPhiN.setNumber(Util.getPhi(p, q));
			} catch (ArithmeticException ex) {
				JOptionPane.showMessageDialog(contentPane, "Can not calculate phi:\n" + ex.getMessage(),
						"Invalid values!", JOptionPane.ERROR_MESSAGE);
			}
			break;
		}
		case "e": {
			BigInteger e = txtE.getNumber();
			if (e == null) {
				return;
			}
			BigInteger q = txtQ.getNumber();
			if (q == null) {
				return;
			}
			BigInteger p = txtP.getNumber();
			if (p == null) {
				return;
			}
			try {
				txtD.setNumber(Util.getD(e, p, q));
			} catch (ArithmeticException ex) {
				JOptionPane.showMessageDialog(contentPane, "Can not calculate d:\n" + ex.getMessage(),
						"Invalid values!", JOptionPane.ERROR_MESSAGE);
			}
			break;
		}
		case "c": {
			BigInteger c = txtC.getNumber();
			if (c == null) {
				return;
			}
			BigInteger n = txtN.getNumber();
			if (n == null) {
				return;
			}
			BigInteger d = txtD.getNumber();
			if (d == null) {
				return;
			}
			txtM.setNumber(Util.decrypt(c, d, n));
			break;
		}
		case "m": {
			BigInteger m = txtM.getNumber();
			if (m == null) {
				return;
			}
			BigInteger n = txtN.getNumber();
			if (n == null) {
				return;
			}
			BigInteger e = txtE.getNumber();
			if (e == null) {
				return;
			}
			txtC.setNumber(Util.encrypt(m, e, n));
			break;
		}
		case "phi": // can't get anything from those (i think)
		case "d":
			break;
		default: {
			System.out.println("Unhandled " + txt.getToolTipText() + ": " + txt.getText());
			break;
		}
		}
	}

}
