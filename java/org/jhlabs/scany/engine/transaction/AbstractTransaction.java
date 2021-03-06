/**
 * 
 */
package org.jhlabs.scany.engine.transaction;

import org.jhlabs.scany.engine.entity.Record;
import org.jhlabs.scany.engine.entity.Relation;
import org.jhlabs.scany.engine.index.AnyIndexerException;
import org.jhlabs.scany.engine.transaction.job.DeleteJob;
import org.jhlabs.scany.engine.transaction.job.InsertJob;
import org.jhlabs.scany.engine.transaction.job.JobQueue;
import org.jhlabs.scany.engine.transaction.job.MergeJob;
import org.jhlabs.scany.engine.transaction.job.UpdateJob;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 7. 오후 1:17:21</p>
 *
 */
public abstract class AbstractTransaction {

	protected Relation relation;

	protected JobQueue jobQueue = new JobQueue();
	
	public AbstractTransaction(Relation relation) {
		this.relation = relation;
	}
	
	public abstract void commit() throws AnyIndexerException;
	
	public abstract void rollback() throws AnyIndexerException;
	
	public void clear() {
		jobQueue.clear();
	}
	
	public void insert(Record record) {
		jobQueue.offer(new InsertJob(record));
	}
	
	public void update(Record record) {
		jobQueue.offer(new UpdateJob(record));
	}
	
	public void merge(Record record) {
		jobQueue.offer(new MergeJob(record));
	}
	
	public void delete(Record record) {
		jobQueue.offer(new DeleteJob(record));
	}

}
