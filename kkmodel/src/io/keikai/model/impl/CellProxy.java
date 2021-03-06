/*

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		2013/12/01 , Created by dennis
}}IS_NOTE

Copyright (C) 2013 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package io.keikai.model.impl;

import java.lang.ref.WeakReference;
import java.util.Locale;

import io.keikai.model.CellRegion;
import io.keikai.model.SCell;
import io.keikai.model.SCellStyle;
import io.keikai.model.SComment;
import io.keikai.model.SHyperlink;
import io.keikai.model.SSheet;
import org.zkoss.poi.ss.formula.eval.ValueEval;
import io.keikai.model.sys.dependency.Ref;
import io.keikai.model.sys.formula.FormulaExpression;

/**
 * 
 * @author dennis
 * @since 3.5.0
 */
class CellProxy extends AbstractCellAdv {
	private static final long serialVersionUID = 1L;
	private WeakReference<AbstractSheetAdv> _sheetRef;
	private int _rowIdx;
	private int _columnIdx;
	AbstractCellAdv _proxy;

	public CellProxy(AbstractSheetAdv sheet, int row, int column) {
		this._sheetRef = new WeakReference<AbstractSheetAdv>(sheet);
		this._rowIdx = row;
		this._columnIdx = column;
	}

	@Override
	public SSheet getSheet() {
		if(_proxy!=null){
			return _proxy.getSheet();
		}
		AbstractSheetAdv sheet = _sheetRef.get();
		if (sheet == null) {
			throw new IllegalStateException(
					"proxy sheet target lost, you should't keep this instance");
		}
		return sheet;
	}

	private void loadProxy() {
		if (_proxy == null) {
			_proxy = (AbstractCellAdv) ((AbstractSheetAdv)getSheet()).getCell(_rowIdx, _columnIdx, false);
		}
	}

	@Override
	public boolean isNull() {
		loadProxy();
		//if any data in data grid and it is not null, you should handle it.
		if(_proxy==null){
			return true;
		}else{
			return _proxy.isNull();
		}
	}

	@Override
	public SCell.CellType getType() {
		loadProxy();
		if(_proxy==null){
			return SCell.CellType.BLANK;
		}else{
			return  _proxy.getType();
		}
	}

	@Override
	public int getRowIndex() {
		loadProxy();
		return _proxy == null ? _rowIdx : _proxy.getRowIndex();
	}

	@Override
	public int getColumnIndex() {
		loadProxy();
		return _proxy == null ? _columnIdx : _proxy.getColumnIndex();
	}

	@Override
	public void setFormulaValue(String formula) {
		loadProxy();
		if (_proxy == null) {
			_proxy = (AbstractCellAdv) ((AbstractRowAdv) ((AbstractSheetAdv)getSheet()).getOrCreateRow(
					_rowIdx)).getOrCreateCell(_columnIdx);
			_proxy.setFormulaValue(formula);
		} else if (_proxy != null) {
			_proxy.setFormulaValue(formula);
		}
	}

	//ZSS-565: Support input with Swedish locale into Formula
	@Override
	public void setFormulaValue(String formula, Locale locale) {
		loadProxy();
		if (_proxy == null) {
			_proxy = (AbstractCellAdv) ((AbstractRowAdv) ((AbstractSheetAdv)getSheet()).getOrCreateRow(
					_rowIdx)).getOrCreateCell(_columnIdx);
			_proxy.setFormulaValue(formula, locale);
		} else if (_proxy != null) {
			_proxy.setFormulaValue(formula, locale);
		}
	}

	@Override
	public void setValue(Object value) {
		loadProxy();
		if (_proxy == null && value != null) {
			_proxy = (AbstractCellAdv) ((AbstractRowAdv) ((AbstractSheetAdv)getSheet()).getOrCreateRow(
					_rowIdx)).getOrCreateCell(_columnIdx);
			_proxy.setValue(value);
		} else if (_proxy != null) {
			_proxy.setValue(value);
		}
	}

	//ZSS-853
	@Override
	protected void setValue(Object value, boolean aString) {
		loadProxy();
		if (_proxy == null && value != null) {
			_proxy = (AbstractCellAdv) ((AbstractRowAdv) ((AbstractSheetAdv)getSheet()).getOrCreateRow(
					_rowIdx)).getOrCreateCell(_columnIdx);
			_proxy.setValue(value, aString);
		} else if (_proxy != null) {
			_proxy.setValue(value, aString);
		}
	}

	@Override
	public Object getValue() {
		loadProxy();
		if(_proxy==null){
			return null;
		}else{
			return  _proxy.getValue();
		}
	}

	@Override
	public String getReferenceString() {
		loadProxy();
		return _proxy == null ? new CellRegion(_rowIdx, _columnIdx).getReferenceString() : _proxy.getReferenceString();
	}

	@Override
	public SCellStyle getCellStyle() {
		return getCellStyle(false);
	}

	@Override
	public SCellStyle getCellStyle(boolean local) {
		loadProxy();
		if (_proxy != null) {
			return _proxy.getCellStyle(local);
		}
		if (local)
			return null;
		AbstractSheetAdv sheet =  ((AbstractSheetAdv)getSheet());
		AbstractRowAdv row = (AbstractRowAdv) sheet.getRow(_rowIdx, false);
		SCellStyle style = null;
		if (row != null) {
			style = row.getCellStyle(true);
		}
		if (style == null) {
			AbstractColumnArrayAdv carr = (AbstractColumnArrayAdv)sheet.getColumnArray(_columnIdx);
			if (carr != null) {
				style = carr.getCellStyle(true);
			}
		}
		if (style == null) {
			style = sheet.getBook().getDefaultCellStyle();
		}
		return style;
	}

	@Override
	public void setCellStyle(SCellStyle cellStyle) {
		loadProxy();
		if (_proxy == null) {
			_proxy = (AbstractCellAdv) ((AbstractRowAdv)  ((AbstractSheetAdv)getSheet()).getOrCreateRow(
					_rowIdx)).getOrCreateCell(_columnIdx);
		}
		_proxy.setCellStyle(cellStyle);
	}

	@Override
	public SCell.CellType getFormulaResultType() {
		loadProxy();
		return _proxy == null ? null : _proxy.getFormulaResultType();
	}

	@Override
	public void clearValue() {
		loadProxy();
		if (_proxy != null)
			_proxy.clearValue();
	}

	@Override
	public void clearFormulaResultCache() {
		loadProxy();
		if (_proxy != null)
			_proxy.clearFormulaResultCache();
	}

	@Override
	protected void evalFormula() {
		loadProxy();
		if (_proxy != null)
			_proxy.evalFormula();
	}

	@Override
	protected Object getValue(boolean eval) {
		loadProxy();
		return _proxy == null ? null : _proxy.getValue(eval);
	}

	@Override
	public void destroy() {
		throw new IllegalStateException(
				"never link proxy object and call it's release");
	}

	@Override
	public void checkOrphan() {
	}

	@Override
	public SHyperlink getHyperlink() {
		loadProxy();
		return _proxy == null ? null : _proxy.getHyperlink();
	}

	@Override
	public void setHyperlink(SHyperlink hyperlink) {
		loadProxy();
		if (_proxy == null) {
			_proxy = (AbstractCellAdv) ((AbstractRowAdv)  ((AbstractSheetAdv)getSheet()).getOrCreateRow(
					_rowIdx)).getOrCreateCell(_columnIdx);
		}
		_proxy.setHyperlink(hyperlink);
	}
	
	@Override
	public SComment getComment() {
		loadProxy();
		return _proxy == null ? null : _proxy.getComment();
	}

	@Override
	public void setComment(SComment comment) {
		loadProxy();
		if (_proxy == null) {
			_proxy = (AbstractCellAdv) ((AbstractRowAdv)  ((AbstractSheetAdv)getSheet()).getOrCreateRow(
					_rowIdx)).getOrCreateCell(_columnIdx);
		}
		_proxy.setComment(comment);
	}

	@Override
	public boolean isFormulaParsingError() {
		loadProxy();
		return _proxy == null ? false : _proxy.isFormulaParsingError();
	}

	@Override
	void setIndex(int newidx) {
		throw new UnsupportedOperationException("readonly");
	}
	@Override
	void setRow(int oldRowIdx,AbstractRowAdv row) {
		throw new UnsupportedOperationException("readonly");
	}
	
	protected Ref getRef(){
		return new RefImpl(this);
	}

	//ZSS-688
	//@since 3.6.0
	@Override
	/*package*/ AbstractCellAdv cloneCell(AbstractRowAdv row) {
		if (_proxy == null) {
			return new CellProxy((AbstractSheetAdv)row.getSheet(), row.getIndex(), this.getColumnIndex());
		} else {
			return _proxy.cloneCell(row);
		}
	}

	@Override
	public void setFormulaResultValue(ValueEval value) {
		loadProxy();
		if (_proxy != null) {
			_proxy.setFormulaResultValue(value);
		}
	}

	@Override
	public void deleteComment() {
		loadProxy();
		if (_proxy != null) {
			_proxy.deleteComment();
		}
	}

	@Override
	public FormulaExpression getFormulaExpression() {
		loadProxy();
		if (_proxy != null) {
			return _proxy.getFormulaExpression();
		}
		return null;
	}

	//ZSS-1116
	@Override
	public void setTextHeight(int height) {
		loadProxy();
		if (_proxy != null) {
			_proxy.setTextHeight(height);
		}
	}

	//ZSS-1116
	@Override
	public int getTextHeight() {
		loadProxy();
		if (_proxy != null) {
			return _proxy.getTextHeight();
		}
		return getSheet().getDefaultRowHeight();
	}
	
	//ZSS-1116
	@Override
	public boolean isCalcAutoHeight() {
		loadProxy();
		if (_proxy != null) {
			return _proxy.isCalcAutoHeight();
		}
		return false;
	}
	
	@Override
	public void setCalcAutoHeight(boolean b) {
		loadProxy();
		if (_proxy != null) {
			_proxy.setCalcAutoHeight(b);
		}
	}

	//ZSS-1171
	@Override
	public void setTextWidth(int width) {
		loadProxy();
		if (_proxy != null) {
			_proxy.setTextWidth(width);
		}
	}

	//ZSS-1171
	@Override
	public int getTextWidth() {
		loadProxy();
		if (_proxy != null) {
			return _proxy.getTextWidth();
		}
		return -1;
	}

	//ZSS-1183
	@Override
	AbstractCellAdv cloneCell(AbstractRowAdv row, SSheet sheet) {
		if (!isNull()) {
			return _proxy.cloneCell(row, sheet);
		}
		return null;
	}

	//ZSS-1193
	@Override
	public CellValue getEvalCellValue(boolean evaluatedVal) {
		loadProxy();
		if(_proxy==null){
			return new CellValue();
		}else{
			return  _proxy.getEvalCellValue(evaluatedVal);
		}
	}
}
