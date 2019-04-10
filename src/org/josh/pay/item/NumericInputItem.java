package org.josh.pay.item;

import org.joshvm.j2me.directUI.Keypad;

import com.josh.pay.window.Event;

public class NumericInputItem extends TextItem {
	private int decimalLen;
	private int integerLen;

	private boolean acceptDot;
	private boolean dotExist;
	private StringBuffer numstr;

	public boolean isAcceptDot() {
		return acceptDot;
	}

	public void setAcceptDot(boolean acceptDot) {
		this.acceptDot = acceptDot;
	}

	private void init() {
		acceptDot = false;
		dotExist = false;
		numstr = new StringBuffer();
		decimalLen = 0;
		integerLen = 0;
	}

	public NumericInputItem(int id, String displayText) {
		super(id, displayText, true);
		init();
		numstr.insert(0, displayText);
	}

	public NumericInputItem(int id) {
		super(id, null, true);
		init();
	}

	public boolean onKeyInputEvent(Event evt) {
		if (evt.type != Event.KEY_PRESSED) {
			return false;
		}
		switch (evt.value) {
		case Keypad.KEY_BACK:
			int len = numstr.length();
			if ((len > 0) && numstr.charAt(len - 1) != '.') {
				if (decimalLen > 0) {
					decimalLen--;
					if (decimalLen == 0) {
						dotExist = true;
					}
				} else if (integerLen > 0) {
					integerLen--;
				}
			} else {
				dotExist = false;
			}
			if (len > 0) {
				numstr = numstr.deleteCharAt(len - 1);
			}
			int spacenum = 10 - numstr.length();
			String spacestr = "";
			for (int i = 0; i < spacenum; i++) {
				spacestr += " ";
			}
			setText(spacestr + numstr.toString());
			show();
			return true;
		case Keypad.KEY_NUM0:
			break;
		case Keypad.KEY_NUM1:
			break;
		case Keypad.KEY_NUM2:
			break;
		case Keypad.KEY_NUM3:
			break;
		case Keypad.KEY_NUM4:
			break;
		case Keypad.KEY_NUM5:
			break;
		case Keypad.KEY_NUM6:
			break;
		case Keypad.KEY_NUM7:
			break;
		case Keypad.KEY_NUM8:
			break;
		case Keypad.KEY_NUM9:
			break;
		case Keypad.KEY_DOT:
			if (dotExist) {
				return true;
			}
			if (acceptDot && (integerLen < 10)) {
				dotExist = true;
			} else {
				return true;
			}
			break;
		default:
			return false;
		}

		if (numstr.length() < 10 && (!dotExist || (dotExist && (decimalLen < 2)))) {
			numstr.append(evt.value);
			if (dotExist) {
				if (evt.value != '.') {
					decimalLen++;
				}
			} else {
				integerLen++;
			}
			int spacenum = 10 - numstr.length();
			String spacestr = "";
			for (int i = 0; i < spacenum; i++) {
				spacestr += " ";
			}
			setText(spacestr + numstr.toString());
			show();
		}
		return true;
	}

	public boolean setFocus(boolean status) {
		if (isHidden()) {
			return false;
		} else {
			isFocused = status;
			return true;
		}
	}

	public void clear() {
		dotExist = false;
		decimalLen = 0;
		integerLen = 0;
		numstr = new StringBuffer();
		setText("");
	}

}
