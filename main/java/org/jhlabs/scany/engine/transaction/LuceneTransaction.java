package org.jhlabs.scany.engine.transaction;

import org.jhlabs.scany.engine.entity.Table;

public class LuceneTransaction extends AbstractAnyTransaction implements AnyTransaction {

	private Table table;

	public LuceneTransaction(Table table) {
		this.table = table;
	}

	public void commit() {
		Job job = jobQueue.peek();
		
		while(job != null) {

			
			committedJobQueue.offer(job);
			
			jobQueue.remove();
			job = jobQueue.peek();
		}
		
	}

	public void rollback() {
		if(committedJobQueue != null && committedJobQueue.size() > 0) {
			
		}
	}
	
}
