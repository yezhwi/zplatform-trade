package com.zlebank.zplatform.trade.dao.impl;

import java.awt.color.CMMException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.commons.bean.TransferBatchQuery;
import com.zlebank.zplatform.commons.dao.impl.AbstractPagedQueryDAOImpl;
import com.zlebank.zplatform.commons.dao.pojo.AccStatusEnum;
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.trade.dao.BankTransferBatchDAO;
import com.zlebank.zplatform.trade.model.PojoBankTransferBatch;

@Repository("transferBatchDAO")
public class BankTransferBatchDAOImpl extends
		AbstractPagedQueryDAOImpl<PojoBankTransferBatch, TransferBatchQuery>
		implements BankTransferBatchDAO {
	private static final Log log = LogFactory
			.getLog(BankTransferBatchDAOImpl.class);

	/**
	 * 通过批次号查找批次信息
	 * 
	 * @param batchno
	 * @return
	 */
	@Override
	@Transactional
	public PojoBankTransferBatch getByBatchNo(String batchno) {
		String queryString = "from PojoBankTransferBatch where batchno = ? ";
		try {
			log.info("queryString:" + queryString);
			Query query = getSession().createQuery(queryString);
			query.setParameter(0, batchno);
			return (PojoBankTransferBatch) query.uniqueResult();

		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CMMException("M001");
		}
	}

	@Override
	@Transactional
	public void updateBatchToTransfer(PojoBankTransferBatch transferBatch) {
		// TODO Auto-generated method stub
		try {
			String hql = "update PojoBankTransferBatch set status = ?,transfertime = ?,requestfilename = ?,responsefilename = ? where batchno= ? ";
			Session session = getSession();
			Query query = session.createQuery(hql);
			query.setParameter(0, transferBatch.getStatus());
			/*query.setParameter(1, transferBatch.getTransfertime());
			query.setParameter(2, transferBatch.getRequestfilename());
			query.setParameter(3, transferBatch.getResponsefilename());
			query.setParameter(4, transferBatch.getBatchno());*/
			query.executeUpdate();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CMMException("M002");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public PojoBankTransferBatch getByReqestFileName(String fileName) {
		List<PojoBankTransferBatch> result = null;
		String queryString = "from PojoBankTransferBatch where requestfilename = ? ";
		try {
			log.info("queryString:" + queryString);
			Query query = getSession().createQuery(queryString);
			query.setParameter(0, fileName);
			result = query.list();
			if (result.size() > 0) {
				return result.get(0);
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CMMException("M001");
		}
		return null;
	}

	/**
	 * 更新批次数据
	 */
	@Override
	@Transactional
	public void updateTransferBatch(PojoBankTransferBatch transferBatch) {
		try {
			getSession().update(transferBatch);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CMMException("M002");
		}
	}

	/**
     * 
     */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<PojoBankTransferBatch> findWaitAccountingTransferBatch() {
		try {
			String hql = "from PojoBankTransferBatch where accstatus = ? ";
			Query query = getSession().createQuery(hql);
			query.setString(0, "01");
			return query.list();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CMMException("M001");
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public int validateBatchResult(String insteadpaybatchno) {
		try {
			String hql = "from PojoBankTransferBatch where status <> ? and insteadpaybatchno = ? ";
			Query query = getSession().createQuery(hql);
			query.setString(0, "00");
			query.setString(1, insteadpaybatchno);
			return query.list().size();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CMMException("M001");
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void updateAccountingResult(String batchno, AccStatusEnum accStatus) {
		try {
			String hql = "update PojoBankTransferBatch set accstatus = ? where batchno = ? ";
			Session session = getSession();
			Query query = session.createQuery(hql);
			query.setParameter(0, accStatus.getCode());
			query.setParameter(1, batchno);
			query.executeUpdate();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CMMException("M002");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public List<PojoBankTransferBatch> findByInsteadpaybatchno(
			String insteadpaybatchno) {
		try {
			String hql = "from PojoBankTransferBatch where insteadpaybatchno = ? ";
			Query query = getSession().createQuery(hql);
			query.setString(0, insteadpaybatchno);
			return query.list();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CMMException("M001");
		}
	}

	/**
	 *
	 * @param e
	 * @return
	 */
	@Override
	protected Criteria buildCriteria(TransferBatchQuery e) {
		Criteria crite = this.getSession().createCriteria(
				PojoBankTransferBatch.class);
		if (e != null) {
			if (StringUtil.isNotEmpty(e.getAccstatus())) {
				crite.add(Restrictions.eq("accstatus", e.getAccstatus()));
			}
			if (StringUtil.isNotEmpty(e.getBatchno())) {
				crite.add(Restrictions.eq("batchno", e.getBatchno()));
			}
			if (StringUtil.isNotEmpty(e.getStatus())) {
				crite.add(Restrictions.eq("status", e.getStatus()));
			}
			if (StringUtil.isNotEmpty(e.getTransfertype())) {
				crite.add(Restrictions.eq("transfertype", e.getTransfertype()));
			}

		}
		crite.addOrder(Order.asc("createtime"));
		return crite;
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public PojoBankTransferBatch getByResponseFileName(String fileName) {
		List<PojoBankTransferBatch> result = null;
		String queryString = "from PojoBankTransferBatch where responsefilename = ? ";
		try {
			log.info("queryString:" + queryString);
			Query query = getSession().createQuery(queryString);
			query.setParameter(0, fileName);
			result = query.list();
			if (result.size() > 0) {
				return result.get(0);
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CMMException("M001");
		}
		return null;
	}
}
