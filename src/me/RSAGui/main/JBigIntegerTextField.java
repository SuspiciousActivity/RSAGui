package me.RSAGui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.regex.Pattern;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

public class JBigIntegerTextField extends JTextField {

	public TextFilter filter;

	public JBigIntegerTextField() {
		super();
		((PlainDocument) getDocument()).setDocumentFilter(filter = new TextFilter());
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (filter.valueChanged) {
					filter.valueChanged = false;
					Main.onTextEnter(JBigIntegerTextField.this);
				}
			}
		});
	}

	public BigInteger getNumber() {
		return getText().isEmpty() ? null : new BigInteger(getText());
	}

	public void setNumber(BigInteger num) {
		setText(num.toString());
	}

	static class TextFilter extends DocumentFilter {

		private static final Pattern numberDecMatcher = Pattern.compile("[0-9]*");
		private static final Pattern numberHexMatcher = Pattern.compile("0x[0-9a-f]+", Pattern.CASE_INSENSITIVE);

		public volatile boolean valueChanged = false;

		private enum NumberType {
			NAN, DEC, HEX;

			public boolean isNumber() {
				return this != NAN;
			}
		}

		private NumberType isValidNumber(String s) {
			if (numberDecMatcher.matcher(s).matches()) {
				return NumberType.DEC;
			} else if (numberHexMatcher.matcher(s).matches()) {
				return NumberType.HEX;
			} else {
				return NumberType.NAN;
			}
		}

		@Override
		public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
				throws BadLocationException {
			Document doc = fb.getDocument();
			StringBuilder sb = new StringBuilder();
			sb.append(doc.getText(0, doc.getLength()));
			sb.insert(offset, string);

			NumberType nt = isValidNumber(sb.toString());
			if (nt.isNumber()) {
				if (nt == NumberType.HEX) {
					string = new BigInteger(string.substring(2), 16).toString();
				}
				super.insertString(fb, offset, string, attr);
				valueChanged = true;
			}
		}

		@Override
		public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
				throws BadLocationException {
			Document doc = fb.getDocument();
			StringBuilder sb = new StringBuilder();
			sb.append(doc.getText(0, doc.getLength()));
			sb.replace(offset, offset + length, text);

			NumberType nt = isValidNumber(sb.toString());
			if (nt.isNumber()) {
				if (nt == NumberType.HEX) {
					text = new BigInteger(text.substring(2), 16).toString();
				}
				super.replace(fb, offset, length, text, attrs);
				valueChanged = true;
			}
		}

		@Override
		public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
			Document doc = fb.getDocument();
			StringBuilder sb = new StringBuilder();
			sb.append(doc.getText(0, doc.getLength()));
			sb.delete(offset, offset + length);

			NumberType nt = isValidNumber(sb.toString());
			if (nt.isNumber()) {
				super.remove(fb, offset, length);
				valueChanged = true;
			}
		}

	}

}
