package com.zlebank.zplatform.trade.dao;

import java.util.List;
import java.util.Map;

import com.zlebank.zplatform.acc.exception.AbstractBusiAcctException;
import com.zlebank.zplatform.acc.exception.AccBussinessException;
import com.zlebank.zplatform.commons.dao.BaseDAO;
import com.zlebank.zplatform.trade.bean.enums.BusinessEnum;
import com.zlebank.zplatform.trade.bean.page.QueryTransferBean;
import com.zlebank.zplatform.trade.model.PojoTranBatch;
import com.zlebank.zplatform.trade.model.PojoTranData;

public interface TransferDataDAO extends BaseDAO<PojoTranData>{
    
    /**
     * 通过批次号和账务状态获取批次明细数据
     * @param batchNo
     * @return
     */
    public List<PojoTranData> findTransDataByBatchNoAndAccstatus(String batchNo);
    
    
    /**
     * 划拨明细查询（分页）
     * @param queryTransferBean
     * @param page
     * @param pageSize
     * @return
     */
    public Map<String, Object> queryTranfersDetaByPage(QueryTransferBean queryTransferBean,int page,int pageSize);
    
    /**
     * 审核驳回业务退款
     * @param transferData
     * @param businessEnum
     * @throws AccBussinessException
     * @throws AbstractBusiAcctException
     * @throws NumberFormatException
     */
    public void businessRefund(PojoTranData transferData,BusinessEnum businessEnum) throws AccBussinessException, AbstractBusiAcctException,NumberFormatException;

    /**
     * 
     * @param tranDataSeqNo
     * @param status
     * @throws NumberFormatException 
     * @throws AbstractBusiAcctException 
     * @throws AccBussinessException 
     */
    public void singleTrailTransfer(String tranDataSeqNo,String status) throws AccBussinessException, AbstractBusiAcctException, NumberFormatException;
    
    /**
     * 
     * @param tranDataSeqNo
     * @return
     */
    public PojoTranData queryTransferData(String tranDataSeqNo);
    
    /**
     * 
     * @param transferBatch
     */
    public void updateBatchTransferSingle(PojoTranBatch transferBatch);
}
