package com.business.intelligence.crawler.baidu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.business.intelligence.model.baidu.*;
import com.business.intelligence.util.MD5;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Parser {
    private static final Logger logger = LoggerFactory.getLogger(Parser.class);

    /**
     * 热销菜品解析
     *
     * @param csvRecords
     * @param shopId
     * @return
     */
    public static List<HotDishes> hotParser(CSVParser csvRecords, String shopId) {
        List<HotDishes> listHot = new ArrayList<>();
        try {
            for (CSVRecord record : csvRecords) {
                HotDishes hot = new HotDishes();
                hot.setSort(Integer.valueOf(record.get(0).trim()));
                hot.setDishesName(record.get(1).trim());
                hot.setSales(Integer.valueOf(record.get(2).trim()));
                hot.setSalesAmount(Double.valueOf(record.get(3).trim()));
                hot.setPrice(Double.valueOf(record.get(4).trim()));
                hot.setSalesAccounted(Double.valueOf(record.get(5).trim()));
                hot.setSalesNumberAccounted(Double.valueOf(record.get(6).trim()));
                hot.setShopId(shopId);
                hot.setCreatTime(new Date());
                hot.setUpdateTime(new Date());
                String id = MD5.md5(shopId + "_" + record.get(1).trim());//商户id+菜品名称MD5后生成主键id
                hot.setId(id);
                listHot.add(hot);
            }
        } catch (Exception e) {
            logger.error("解析热销菜品出错", e);
        }
        logger.info("百度热销菜品下载内容：" + JSONObject.toJSONString(listHot));
        return listHot;
    }

    /**
     * 经营数据解析
     *
     * @param csvRecords
     * @param shopId
     * @return
     */
    public static List<BusinessData> bdParser(CSVParser csvRecords, String shopId) {
        List<BusinessData> bdList = new ArrayList<>();
        try {
            for (CSVRecord record : csvRecords) {
                BusinessData bd = new BusinessData();
                bd.setTime(record.get(0).trim());
                bd.setVisitingRs(Integer.valueOf(record.get(1).trim()));
                bd.setVisitingCs(Integer.valueOf(record.get(2).trim()));
                bd.setVisitingPer(Double.valueOf(record.get(3).trim()));
                bd.setExposureRs(Integer.valueOf(record.get(4).trim()));
                bd.setExposureCs(Integer.valueOf(record.get(5).trim()));
                bd.setOrderAmount(Integer.valueOf(record.get(6).trim()));
                bd.setOrderConvert(Double.valueOf(record.get(7).trim().replace("%", "")));
                bd.setShopRanking(Double.valueOf(record.get(8).replace("%", "")));
                bd.setShopId(shopId);
                bd.setCreatTime(new Date());
                bd.setUpdateTime(new Date());
                String id = MD5.md5(shopId + "_" + record.get(0).trim());//商户id+日期MD5后生成主键id
                bd.setId(id);
                bdList.add(bd);
            }
        } catch (Exception e) {
            logger.error("解析经营数据出错", e);
        }
        logger.info("百度曝光数据下载内容：" + JSONObject.toJSONString(bdList));
        return bdList;
    }

    /**
     * 百度商户提现解析
     *
     * @param csvRecords
     * @param shopId
     * @return
     */
    public static List<ShopWthdrawal> swParser(CSVParser csvRecords, String shopId) {
        List<ShopWthdrawal> swList = new ArrayList<>();
        try {
            for (CSVRecord record : csvRecords) {
                ShopWthdrawal sw = new ShopWthdrawal();
                sw.setBillDate(record.get(0).trim());
                sw.setAccountDate(record.get(1).trim());
                sw.setSerialNumber(record.get(2).trim());
                sw.setTurnSerialNumber(record.get(3).trim());
                sw.setAccountType(record.get(4).trim());
                sw.setAmount(Double.valueOf(record.get(5).trim()));
                sw.setAccountBalance(Double.valueOf(record.get(6).trim()));
                sw.setFreezeAmount(Double.valueOf(record.get(7).trim()));
                sw.setSumFreezeAmount(Double.valueOf(record.get(8).trim()));
                sw.setPaymentAccount(record.get(9).trim());
                sw.setPaymentName(record.get(10).trim());
                sw.setPaymentStatus(record.get(11).trim());
                sw.setNote(record.get(12).trim());
                sw.setApplicationDate(record.get(13).trim());
                sw.setShopId(shopId);
                sw.setCreatTime(new Date());
                sw.setUpdateTime(new Date());
                String id = MD5.md5(shopId + "_" + record.get(0).trim() + "_" + record.get(2).trim());//商户id+账单日期+交易流水号MD5后生成主键id
                sw.setId(id);
                swList.add(sw);
            }
        } catch (Exception e) {
            logger.error("解析商户提现账户出错", e);
        }
        logger.info("百度自动提现账户页面下载内容：" + JSONObject.toJSONString(swList));
        return swList;
    }

    /**
     * 百度已入账解析
     *
     * @param csvRecords
     * @param shopId
     * @return
     */
    public static List<BookedTable> btParser(CSVParser csvRecords, String shopId) {
        List<BookedTable> btList = new ArrayList<>();
        try {
            for (CSVRecord record : csvRecords) {
                BookedTable bt = new BookedTable();
                bt.setOrderSortNumber(Integer.valueOf(record.get(0).toString()));
                bt.setOrderNumber(record.get(1).toString());
                bt.setActualPay(record.get(2).toString());
                bt.setFinancialType(record.get(3).toString());
                bt.setSerialNumber(record.get(4).toString());
                bt.setBusinessNumbet(record.get(5).toString());
                bt.setOrderStatus(record.get(6).toString());
                bt.setOrderTime(record.get(7).toString());
                bt.setFinancialTime(record.get(8).toString());
                bt.setYsMax(Double.valueOf(record.get(9).toString()));
                bt.setYxMin(Double.valueOf(record.get(10).toString()));
                bt.setAccountBalance(Double.valueOf(record.get(11).toString()));
                bt.setNote(record.get(12).toString());
                bt.setSecondarySubject(record.get(13).toString());
                bt.setBusinessType(record.get(14).toString());
                bt.setPressure(record.get(15).toString());
                bt.setOrderType(record.get(16).toString());
                bt.setLossBears(record.get(17).toString());
                bt.setFoodEffect(Double.valueOf(record.get(18).toString()));
                bt.setBoxesEffect(Double.valueOf(record.get(19).toString()));
                bt.setSubsidieEffect(Double.valueOf(record.get(20).toString()));
                bt.setCommissionEffect(Double.valueOf(record.get(21).toString()));
                bt.setShippEffect(Double.valueOf(record.get(22).toString()));
                bt.setBdSubsidieEffect(Double.valueOf(record.get(23).toString()));
                bt.setSubsidieAgentsEffect(Double.valueOf(record.get(24).toString()));
                bt.setUserPayEffect(Double.valueOf(record.get(25).toString()));
                bt.setBillAmount(Double.valueOf(record.get(26).toString()));
                bt.setFoodDonEffect(Double.valueOf(record.get(27).toString()));
                bt.setBoxesDonEffect(Double.valueOf(record.get(28).toString()));
                bt.setSubsidieDonEffect(Double.valueOf(record.get(29).toString()));
                bt.setCommissionDonEffect(Double.valueOf(record.get(30).toString()));
                bt.setShippDonEffect(Double.valueOf(record.get(31).toString()));
                bt.setBdSubsidieDonEffect(Double.valueOf(record.get(32).toString()));
                bt.setSubsidieAgentsDonEffect(Double.valueOf(record.get(33).toString()));
                bt.setUserPayDonEffect(Double.valueOf(record.get(34).toString()));
                bt.setBillDonAmount(Double.valueOf(record.get(35).toString()));//
                bt.setSupplier(record.get(36).toString());
                bt.setLogistic(record.get(37).toString());
                bt.setAgent(record.get(38).toString());
                bt.setKnight(record.get(39).toString());
                bt.setShopId(shopId);
                bt.setCreatTime(new Date());
                bt.setUpdateTime(new Date());
                String id = MD5.md5(shopId + "_" + record.get(1).toString() + "_" + record.get(4).toString());//商户id+订单号+交易流水号MD5后生成主键id
                bt.setId(id);
                btList.add(bt);
            }
        } catch (Exception e) {
            logger.error("解析百度已入账出错", e);
        }
        logger.info("百度现金账户流水明细下载内容：" + JSONObject.toJSONString(btList));
        return btList;
    }

    /**
     * 百度评论解析
     *
     * @param context
     * @param shopId
     * @return
     */
    public static List<Comment> ctParser(String context, String shopId) {
        List<Comment> ctList = new ArrayList<>();
        try {
            if (!context.isEmpty()) {
                JSONObject json = JSONObject.parseObject(context);
                JSONObject body = json.getJSONObject("body");
                String errno = body.getString("errno");
                if ("0".equals(errno)) {
                    JSONObject data = body.getJSONObject("data");
                    JSONArray commentList = data.getJSONArray("comment_list");
                    for (int i = 0; i < commentList.size(); i++) {
                        JSONObject ctjson = commentList.getJSONObject(i);
                        Comment ct = new Comment();
                        ct.setCommentId(ctjson.getString("comment_id"));
                        ct.setContent(ctjson.getString("content"));
                        ct.setScore(ctjson.getDouble("score"));
                        ct.setServiceScore(ctjson.getDouble("service_score"));
                        ct.setDishScore(ctjson.getDouble("dish_score"));
                        ct.setRecommendDishes(ctjson.getString("recommend_dishes"));
                        ct.setBadDishes(ctjson.getString("bad_dishes"));
                        ct.setCommentLabels(ctjson.getString("comment_labels"));
                        ct.setReplyContent(ctjson.getString("reply_content"));
                        ct.setCreateTime(ctjson.getString("create_time"));
                        ct.setUserName(ctjson.getString("username"));
                        ct.setShopId(ctjson.getString("shop_id"));
                        ct.setShopName(ctjson.getString("shop_name"));
                        ct.setCostTime(ctjson.getString("cost_time"));
                        ct.setCreatTime(new Date());
                        ct.setUpdateTime(new Date());
                        String id = MD5.md5(shopId + "_" + ctjson.getString("comment_id") + "_" + ctjson.getString("create_time"));//商户id+评论id+评论时间Md5后生成主键id
                        ct.setId(id);
                        ctList.add(ct);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("解析百度评论出错", e);
        }
        logger.info("百度评论下载内容：" + JSONObject.toJSONString(ctList));
        return ctList;
    }

    public static List<OrderDetails> odParser(String context, String shopId) {
        List<OrderDetails> odList = new ArrayList<>();
        try {
            if (!context.isEmpty()) {
                JSONObject json = JSONObject.parseObject(context);
                JSONObject body = json.getJSONObject("body");
                String errno = body.getString("errno");
                if ("0".equals(errno)) {
                    JSONObject data = body.getJSONObject("data");
                    OrderDetails od = new OrderDetails();
                    //获取用户信息
                    JSONObject user = data.getJSONObject("user");
                    od.setName(user.getString("name"));
                    od.setPhone(user.getString("phone"));
                    od.setGender(user.getInteger("gender"));
                    od.setAddress(user.getString("address"));
                    od.setProvince(user.getString("province"));
                    od.setCity(user.getString("city"));
                    od.setDistrict(user.getString("district"));
                    od.setLongitude(user.getString("longitude"));
                    od.setLatitude(user.getString("latitude"));
                    //获取商户信息
                    JSONObject shop = data.getJSONObject("shop");
                    od.setShopId(shop.getString("shop_id"));
                    od.setBaiduShopId(shop.getString("baidu_shop_id"));
                    od.setBaiduName(shop.getString("name"));
                    //获取订单商品信息组
                    JSONArray products = data.getJSONArray("products");
                    od.setProducts(products.toJSONString());
                    //获取优惠信息
                    JSONArray discount = data.getJSONArray("discount");
                    od.setDiscount(discount.toJSONString());
                    //获取退款信息
                    JSONArray partinfo = data.getJSONArray("part_refund_info");
                    od.setPartEefundInfo(partinfo.toJSONString());
                    //获取订单信息
                    JSONObject order = data.getJSONObject("order");
                    od.setOrderId(order.getString("order_id"));
                    od.setSendImmediately(order.getString("send_immediately"));
                    od.setOrderIndex(order.getIntValue("order_index"));
                    od.setStatus(order.getIntValue("status"));
                    od.setExpectTimeMode(order.getIntValue("expect_time_mode"));
                    od.setSendTime(order.getString("send_time"));
                    od.setPickupTime(order.getString("pickup_time"));
                    od.setAtshopTime(order.getString("atshop_time"));
                    od.setDeliveryTime(order.getString("delivery_time"));
                    od.setDeliveryPhone(order.getString("delivery_phone"));
                    od.setFinishedTime(order.getString("finished_time"));
                    od.setConfirmTime(order.getString("confirm_time"));
                    od.setCancelTime(order.getString("cancel_time"));
                    od.setSendFee(order.getIntValue("send_fee"));
                    od.setPackageFee(order.getIntValue("package_fee"));
                    od.setDiscountFee(order.getIntValue("discount_fee"));
                    od.setShopFee(order.getIntValue("shop_fee"));
                    od.setTotalFee(order.getIntValue("total_fee"));
                    od.setUserFee(order.getIntValue("user_fee"));
                    od.setPayType(order.getString("pay_type"));
                    od.setNeedInvoice(order.getString("need_invoice"));
                    od.setInvoiceTitle(order.getString("invoice_title"));
                    od.setTaxerId(order.getString("taxer_id"));
                    od.setRemark(order.getString("remark"));
                    od.setDeliveryParty(order.getString("delivery_party"));
                    od.setCreateTime(order.getString("create_time"));
                    od.setMealNum(order.getString("meal_num"));
                    od.setResponsibleParty(order.getString("responsible_party"));
                    od.setCommission(order.getString("commission"));
                    od.setCreatTime(new Date());
                    od.setUpdateTime(new Date());
                    String id = MD5.md5(shopId + "_" + order.getString("order_id") + "_" + order.getString("order_index"));//商户id+订单id+订单当日流水号MD5后生成主键id
                    od.setId(id);
                    odList.add(od);

                }
            }
        } catch (Exception e) {
            logger.error("解析百度订单详情出错", e);
        }
        logger.info("百度订单详情下载内容：" + JSONObject.toJSONString(odList));
        return odList;
    }

}
