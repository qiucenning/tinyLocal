package org.tinygroup.tinydb.testcase.transaction;

import org.springframework.transaction.TransactionStatus;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.DbOperatorFactory;
import org.tinygroup.tinydb.exception.TinyDbException;
import org.tinygroup.tinydb.operator.DBOperator;
import org.tinygroup.tinydb.operator.TransactionCallBack;
import org.tinygroup.tinydb.test.BaseTest;

public class SelfTransactionTest extends BaseTest{
	

	private Bean getAnimalBean() {
		Bean bean = new Bean(ANIMAL);
		bean.setProperty("id", "beanId");
		bean.setProperty("name", "1234");
		bean.setProperty("length", "1234");
		return bean;
	}


	public void testRollbackTransaction() throws TinyDbException{
		Bean bean=getAnimalBean();
		DBOperator operator=getOperator();
		operator.delete(bean);//先刪除
		try {
			operator.beginTransaction();
			operator.insert(bean);
			operator.insert(bean);
			operator.insert(bean);
			throwException();
			operator.commitTransaction();
		} catch (Exception e) {
			operator.rollbackTransaction();
		}
		int accout=operator.account("select count(*) from animal");
		assertEquals(0, accout);
		operator.delete(bean);//最后刪除
	}
	
	public void testTransactionCallBack() throws TinyDbException{
		final DBOperator operator=getOperator();
		final Bean bean=getAnimalBean();
		operator.delete(bean);//先刪除
		try {
			operator.execute(new TransactionCallBack() {
				public Object callBack(TransactionStatus status) throws TinyDbException {
					operator.insert(bean);
					operator.insert(bean);
					operator.insert(bean);
					throwException();
					return null;
				}
			});
		} catch (Exception e) {
		}
		int accout=operator.account("select count(*) from animal");
		assertEquals(0, accout);
		operator.execute("delete from animal");//刪除
	}
	
//	public void testNoTransation(){
//		Bean bean=getAnimalBean();
//		DBOperator operator=getOperator();
//		operator.delete(bean);//先刪除
//		try {
//			operator.insert(bean);
//			operator.insert(bean);
//			operator.insert(bean);
//			throwException();
//		} catch (Exception e) {
//		}
//		int accout=operator.account("select count(*) from animal");
//		assertEquals(3, accout);
//		operator.delete(bean);//最后刪除
//	}
	
	private void throwException(){
		throw new RuntimeException("主动抛出错误异常");
	}
    
	public void testNestedTransaction() throws TinyDbException{
		DBOperator operator=manager.getDbOperator();
		operator.execute("delete from animal");//刪除
		Bean bean=getAnimalBean();
		try {
			operator.beginTransaction();
			operator.insert(bean);
			DBOperator operator2=manager.getNewDbOperator();
			try {
				operator2.beginTransaction();
				operator2.insert(bean);
				operator2.insert(bean);
				operator2.insert(bean);
				throwException();
				operator2.commitTransaction();
			} catch (Exception e) {
				operator2.rollbackTransaction();
			}
			operator.commitTransaction();
		} catch (Exception e) {
			operator.rollbackTransaction();
		}
		int accout=operator.account("select count(*) from animal");
		assertEquals(1, accout);
		operator.execute("delete from animal");//刪除
		
	}
}
