package com.business.intelligence.crawler.baidu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.business.intelligence.model.baidu.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springfox.documentation.spring.web.json.Json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Parser {
    private static final Logger logger = LoggerFactory.getLogger(Parser.class);

    /**
     * 热销菜品解析
     *
     * @param list
     * @param shopId
     * @return
     */
    public static List<HotDishes> hotParser(List<String> list, String shopId) {
        List<HotDishes> listHot = new ArrayList<>();
        try {
            for (int i = 0; i < list.size(); i++) {

                String content = list.get(i);
                String[] array = content.split(",");
                HotDishes hot = new HotDishes();
                if (array.length == 7) {
                    hot.setSort(Integer.valueOf(array[0].trim()));
                    hot.setDishesName(array[1].trim());
                    hot.setSales(Integer.valueOf(array[2].trim()));
                    hot.setSalesAmount(Double.valueOf(array[3].trim()));
                    hot.setPrice(Double.valueOf(array[4].trim()));
                    hot.setSalesAccounted(Double.valueOf(array[5].trim()));
                    hot.setSalesNumberAccounted(Double.valueOf(array[6].trim()));
                    hot.setShopId(shopId);
                    hot.setCreatTime(new Date());
                    hot.setUpdateTime(new Date());
                    listHot.add(hot);
                }
            }
        } catch (Exception e) {
            logger.error("解析热销菜品出错", e);
        }
        return listHot;
    }

    /**
     * 经营数据解析
     *
     * @param list
     * @param shopId
     * @return
     */
    public static List<BusinessData> bdParser(List<String> list, String shopId) {
        List<BusinessData> bdList = new ArrayList<>();
        try {
            for (int i = 0; i < list.size(); i++) {
                String content = list.get(i);
                String[] array = content.split(",");
                BusinessData bd = new BusinessData();
                if (array.length == 9) {
                    bd.setTime(array[0].trim());
                    bd.setVisitingRs(Integer.valueOf(array[1].trim()));
                    bd.setVisitingCs(Integer.valueOf(array[2].trim()));
                    bd.setVisitingPer(Long.valueOf(array[3].trim()));
                    bd.setExposureRs(Integer.valueOf(array[4].trim()));
                    bd.setExposureCs(Integer.valueOf(array[5].trim()));
                    bd.setOrderAmount(Integer.valueOf(array[6].trim()));
                    bd.setOrderConvert(Double.valueOf(array[7].trim()));
                    bd.setShopRanking(Double.valueOf(array[8].trim()));
                    bd.setShopId(shopId);
                    bd.setCreatTime(new Date());
                    bd.setUpdateTime(new Date());
                    bdList.add(bd);
                }
            }
        } catch (Exception e) {
            logger.error("解析经营数据出错", e);
        }
        return bdList;
    }

    /**
     * 百度商户提现解析
     *
     * @param list
     * @param shopId
     * @return
     */
    public static List<ShopWthdrawal> swParser(List<String> list, String shopId) {
        List<ShopWthdrawal> swList = new ArrayList<>();
        try {
            for (int i = 0; i < list.size(); i++) {
                String content = list.get(i);
                String[] array = content.split(",");
                ShopWthdrawal sw = new ShopWthdrawal();
                if (array.length == 14) {
                    sw.setBillDate(array[0].trim());
                    sw.setAccountDate(array[1].trim());
                    sw.setSerialNumber(array[2].trim());
                    sw.setTurnSerialNumber(array[3].trim());
                    sw.setAccountType(array[4].trim());
                    sw.setAmount(Double.valueOf(array[5].trim()));
                    sw.setAccountBalance(Double.valueOf(array[6].trim()));
                    sw.setFreezeAmount(Double.valueOf(array[7].trim()));
                    sw.setSumFreezeAmount(Double.valueOf(array[8].trim()));
                    sw.setPaymentAccount(array[9].trim());
                    sw.setPaymentName(array[10].trim());
                    sw.setPaymentStatus(array[11].trim());
                    sw.setNote(array[12].trim());
                    sw.setApplicationDate(array[13].trim());
                    sw.setShopId(shopId);
                    sw.setCreatTime(new Date());
                    sw.setUpdateTime(new Date());
                    swList.add(sw);
                }
            }
        } catch (Exception e) {
            logger.error("解析商户提现账户出错", e);
        }
        return swList;
    }

    /**
     * 百度已入账解析
     *
     * @param list
     * @param shopId
     * @return
     */
    public static List<BookedTable> btParser(List<String> list, String shopId) {
        List<BookedTable> btList = new ArrayList<>();
        try {
            for (int i = 0; i < list.size(); i++) {
                String content = list.get(i);
                String[] array = content.split(",");
                BookedTable bt = new BookedTable();
                if (array.length == 38) {
                    bt.setOrderSortNumber(Integer.valueOf(array[0].toString()));
                    bt.setOrderNumber(array[1].toString());
                    bt.setActualPay(array[2].toString());
                    bt.setFinancialType(array[3].toString());
                    bt.setSerialNumber(array[4].toString());
                    bt.setBusinessNumbet(array[5].toString());
                    bt.setOrderStatus(array[6].toString());
                    bt.setOrderTime(array[7].toString());
                    bt.setFinancialTime(array[8].toString());
                    bt.setYsMax(Double.valueOf(array[9].toString()));
                    bt.setYxMin(Double.valueOf(array[10].toString()));
                    bt.setAccountBalance(Double.valueOf(array[11].toString()));
                    bt.setNote(array[12].toString());
                    bt.setSecondarySubject(array[13].toString());
                    bt.setBusinessType(array[14].toString());
                    bt.setPressure(array[15].toString());
                    bt.setOrderType(array[16].toString());
                    bt.setLossBears(array[17].toString());
                    bt.setFoodEffect(Double.valueOf(array[18].toString()));
                    bt.setBoxesEffect(Double.valueOf(array[19].toString()));
                    bt.setSubsidieEffect(Double.valueOf(array[20].toString()));
                    bt.setCommissionEffect(Double.valueOf(array[21].toString()));
                    bt.setShippEffect(Double.valueOf(array[22].toString()));
                    bt.setBdSubsidieEffect(Double.valueOf(array[23].toString()));
                    bt.setSubsidieAgentsEffect(Double.valueOf(array[24].toString()));
                    bt.setUserPayEffect(Double.valueOf(array[25].toString()));
                    bt.setFoodDonEffect(Double.valueOf(array[26].toString()));
                    bt.setBoxesDonEffect(Double.valueOf(array[27].toString()));
                    bt.setSubsidieDonEffect(Double.valueOf(array[28].toString()));
                    bt.setCommissionDonEffect(Double.valueOf(array[29].toString()));
                    bt.setShippDonEffect(Double.valueOf(array[30].toString()));
                    bt.setBdSubsidieDonEffect(Double.valueOf(array[31].toString()));
                    bt.setSubsidieAgentsDonEffect(Double.valueOf(array[32].toString()));
                    bt.setUserPayDonEffect(Double.valueOf(array[33].toString()));
                    bt.setSupplier(array[34].toString());
                    bt.setLogistic(array[35].toString());
                    bt.setAgent(array[36].toString());
                    bt.setKnight(array[37].toString());
                    bt.setShopId(shopId);
                    bt.setCreatTime(new Date());
                    bt.setUpdateTime(new Date());
                    btList.add(bt);
                }
            }
        } catch (Exception e) {
            logger.error("解析百度已入账出错", e);
        }
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
                        ctList.add(ct);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("解析商户提现账户出错", e);
        }
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
                    odList.add(od);

                }
            }
        } catch (Exception e) {
            logger.error("解析商户提现账户出错", e);
        }
        return odList;
    }

}
