package com.yjq.programmer.service.common.impl;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.common.HouseDao;
import com.yjq.programmer.dao.common.OrderTimeDao;
import com.yjq.programmer.dao.common.UserDao;
import com.yjq.programmer.enums.HouseCategoryEnum;
import com.yjq.programmer.enums.HouseStateEnum;
import com.yjq.programmer.enums.UserRoleEnum;
import com.yjq.programmer.form.HouseSearchForm;
import com.yjq.programmer.pojo.common.House;
import com.yjq.programmer.pojo.common.User;
import com.yjq.programmer.service.common.IHouseService;
import com.yjq.programmer.service.common.IUserService;
import com.yjq.programmer.service.home.IChatService;
import com.yjq.programmer.utils.CommonUtil;
import com.yjq.programmer.utils.ValidateEntityUtil;
import com.yjq.programmer.vo.ResponseVo;
import com.yjq.programmer.ws.ChatEndpoint;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-30 18:05
 */
/**
 * 房屋service接口实现类
 */
@Service
@Transactional
public class HouseServiceImpl implements IHouseService {

    @Autowired
    private HouseDao houseDao;

    @Autowired
    private OrderTimeDao orderTimeDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private IChatService chatService;

    @Autowired
    private IUserService userService;

    @Qualifier("elasticsearchClient")
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //redis键名模板
    private final static String HOT_WORD_REDIS_KEY_TEMPLATE = "hot_word";

    private final static String USER_CHAT_REDIS_KEY_TEMPLATE = "user_chat";

    private final static String AGENT_CHAT_REDIS_KEY_TEMPLATE = "agent_chat";

    @Value("${yjq.hot-word.url}")
    private String hotWordPath; //热词文本保存位置

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private Gson gson = new Gson();

    @Override
    public Map<String, Object> getHouseList(Integer page, Integer rows, String info, Integer state, HttpServletRequest request) {
        Map<String, Object> ret = new HashMap<>();
        Map<String, Object> queryMap = new HashMap<>();
        User user = userService.getUserInfo(request).getData();
        if(user == null || user.getRoleId() == null){
            return ret;
        }
        if(!UserRoleEnum.ADMIN.getCode().equals(user.getRoleId())){
            //如果不是管理员角色 只能看到自己发布的房屋信息
            queryMap.put("userId", user.getId());
        }
        queryMap.put("info", info);
        queryMap.put("state", state);
        ret.put("rows", houseDao.getHouseList(queryMap));
        ret.put("total", houseDao.getAllTotal(queryMap));
        return ret;
    }

    @Override
    public ResponseVo<Boolean> addHouse(House house, HttpServletRequest request) {
        if(house == null){
            return ResponseVo.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(house);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseVo.errorByMsg(validate);
        }
        // 把当前登录用户的id和角色id取出来
        String roleId = (String) request.getAttribute("roleId");
        String id = (String) request.getAttribute("id");
        if(CommonUtil.isEmpty(id)){
            return ResponseVo.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        // 判断当前角色是中介 直接把当前用户id设为房屋所属中介的id
        if(UserRoleEnum.HOUSE_AGENT.getCode().equals(Integer.valueOf(roleId))){
            house.setUserId(Long.valueOf(id));
        }
        // 判断当前房屋所属中介id是否为空
        if(house.getUserId() == null){
            return ResponseVo.errorByMsg(CodeMsg.HOUSE_USER_EMPTY);
        }

        // 设置房屋状态、创建时间、更新时间
        house.setState(HouseStateEnum.WAIT_AUDIT.getCode());
        house.setCreateTime(new Date());
        house.setUpdateTime(new Date());

        // 向数据库中添加房屋信息
        if(houseDao.insert(house) == 0){
            return ResponseVo.errorByMsg(CodeMsg.HOUSE_ADD_ERROR);
        }

        return ResponseVo.successByMsg(true,"成功添加房屋信息！");
    }

    @Override
    public ResponseVo<Boolean> editHouse(House house, HttpServletRequest request) {
        if(house == null){
            return ResponseVo.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(house);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseVo.errorByMsg(validate);
        }
        // 把当前登录用户的id和角色id取出来
        String roleId = (String) request.getAttribute("roleId");
        String id = (String) request.getAttribute("id");
        if(CommonUtil.isEmpty(id)){
            return ResponseVo.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        // 判断当前角色是中介 直接把当前用户id设为房屋所属中介的id
        if(UserRoleEnum.HOUSE_AGENT.getCode().equals(Integer.valueOf(roleId))){
            house.setUserId(Long.valueOf(id));
        }
        // 判断当前房屋所属中介id是否为空
        if(house.getUserId() == null){
            return ResponseVo.errorByMsg(CodeMsg.HOUSE_USER_EMPTY);
        }

        // 设置房屋状态、更新时间
        house.setState(HouseStateEnum.WAIT_AUDIT.getCode());
        house.setUpdateTime(new Date());

        //修改数据库中的房屋信息
        houseDao.updateById(house);

        return ResponseVo.successByMsg(true,"成功修改房屋信息！");
    }

    @Override
    public ResponseVo<Boolean> deleteHouse(String ids) {
        if(CommonUtil.isEmpty(ids)){
            return ResponseVo.errorByMsg(CodeMsg.DATA_ERROR);
        }
        String[] split = ids.split(",");
        List<String> idsList = Arrays.asList(split);
        //删除数据库中的用户信息
        houseDao.deleteBatchIds(idsList);
        //删除这些房屋对应的预约信息
        orderTimeDao.deleteByHouseIdList(idsList);
        return ResponseVo.successByMsg(true,"成功删除房屋信息！");
    }

    @Override
    public ResponseVo<Boolean> editStateByAgent(Long id, Integer state) {
        if(id == null || state == null){
            return ResponseVo.errorByMsg(CodeMsg.DATA_ERROR);
        }
        House selectedHouse = houseDao.selectById(id);
        if(selectedHouse == null){
            return ResponseVo.errorByMsg(CodeMsg.HOUSE_NOT_EXIST);
        }
        //修改数据库中房屋状态信息
        selectedHouse.setState(state);
        houseDao.updateById(selectedHouse);

        return ResponseVo.successByMsg(true,"成功修改房屋状态信息！");
    }

    @Override
    public ResponseVo<Boolean> editStateByAdmin(Long id, Integer state) {
        if(id == null || state == null){
            return ResponseVo.errorByMsg(CodeMsg.DATA_ERROR);
        }
        House selectedHouse = houseDao.selectById(id);
        if(selectedHouse == null){
            return ResponseVo.errorByMsg(CodeMsg.HOUSE_NOT_EXIST);
        }
        //修改数据库中房屋状态信息
        selectedHouse.setState(state);
        houseDao.updateById(selectedHouse);

        return ResponseVo.successByMsg(true,"成功修改房屋状态信息！");
    }

    @Override
    public ResponseVo<List<House>> getData() {
        //从MySQL数据库中获取所有待出租的房屋信息数据
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("state", HouseStateEnum.WAIT_LEASE.getCode());
        List<House> houseList = houseDao.getHouseList(queryMap);
        return ResponseVo.success(houseList);
    }

    @Override
    public ResponseVo<List<House>> getESData(HouseSearchForm houseSearchForm) throws IOException {
        //从ES中获取所有数据
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置高亮处理
        HighlightBuilder highlightBuilder =  new HighlightBuilder();
        highlightBuilder.field("info").field("location").preTags("<span style='color:red;'>").postTags("</span>");
        //根据不同的搜索条件进行封装
        if(houseSearchForm != null){
            //价格从低到高排序
            if(houseSearchForm.getIsMoneyOrder() != null && houseSearchForm.getIsMoneyOrder()){
                searchSourceBuilder.sort("money", SortOrder.ASC);
            }
            //更新时间从早到晚排序
            if(houseSearchForm.getIsUpdateTimeOrder() != null && houseSearchForm.getIsUpdateTimeOrder()){
                searchSourceBuilder.sort("updateTime", SortOrder.DESC);
            }
            //声明BoolQueryBuilder对象
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            //通过租金范围进行搜索
            if(!CommonUtil.isEmpty(houseSearchForm.getMoney()) && !"不限".equals(houseSearchForm.getMoney())){
                //将搜索租金根据'-'分割
                String[] splitMoney = houseSearchForm.getMoney().split("-");
                if(splitMoney.length == 2){
                    RangeQueryBuilder rangeQueryBuilderByMoney = QueryBuilders
                            .rangeQuery("money")
                            .from(new BigDecimal(splitMoney[0].substring(0, splitMoney[0].length()-1))).to(new BigDecimal(splitMoney[1].substring(0, splitMoney[1].length()-1)));
                    //拼接到boolQueryBuilder中
                    boolQueryBuilder.must(rangeQueryBuilderByMoney);
                }else if(splitMoney.length == 1){
                    String money = splitMoney[0].substring(0, splitMoney[0].length()-3);
                    RangeQueryBuilder rangeQueryBuilderByMoney = QueryBuilders
                            .rangeQuery("money")
                            .gt(new BigDecimal(money));
                    //拼接到boolQueryBuilder中
                    boolQueryBuilder.must(rangeQueryBuilderByMoney);
                }
            }
            //通过面积范围进行搜索
            if(!CommonUtil.isEmpty(houseSearchForm.getArea()) && !"不限".equals(houseSearchForm.getArea())){
                //将搜索面积根据'-'分割
                String[] splitArea = houseSearchForm.getArea().split("-");
                if(splitArea.length == 2){
                    RangeQueryBuilder rangeQueryBuilderByArea = QueryBuilders
                            .rangeQuery("area")
                            .from(new BigDecimal(splitArea[0].substring(0, splitArea[0].length()-2))).to(new BigDecimal(splitArea[1].substring(0, splitArea[1].length()-2)));
                    //拼接到boolQueryBuilder中
                    boolQueryBuilder.must(rangeQueryBuilderByArea);
                }else if(splitArea.length == 1){
                    String area = splitArea[0].substring(0, splitArea[0].length()-4);
                    RangeQueryBuilder rangeQueryBuilderByArea = QueryBuilders
                            .rangeQuery("area")
                            .gt(new BigDecimal(area));
                    //拼接到boolQueryBuilder中
                    boolQueryBuilder.must(rangeQueryBuilderByArea);
                }
            }
            //通过朝向搜索
            if(!CommonUtil.isEmpty(houseSearchForm.getOrientation()) && !"不限".equals(houseSearchForm.getOrientation())){
                //拼接到boolQueryBuilder中
                boolQueryBuilder.must(QueryBuilders.termQuery("orientation",houseSearchForm.getOrientation()));
            }
            //通过出售方式进行搜索
            if(!CommonUtil.isEmpty(houseSearchForm.getCategory()) && !"不限".equals(houseSearchForm.getCategory())){
                //拼接到boolQueryBuilder中
                boolQueryBuilder.must(QueryBuilders.termQuery("category", Integer.valueOf(houseSearchForm.getCategory())));
            }
            //通过用户输入内容进行搜索
            if(!CommonUtil.isEmpty(houseSearchForm.getContext())){
                //从房屋简介和房屋位置中进行搜索
                QueryBuilder query = QueryBuilders.boolQuery()
                        .should(QueryBuilders.termQuery("info",houseSearchForm.getContext()))
                        .should(QueryBuilders.termQuery("location",houseSearchForm.getContext()));
                boolQueryBuilder.must(query);
                //判断用户输入的内容是否为热词
                HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
                String value = opsForHash.get(HOT_WORD_REDIS_KEY_TEMPLATE, houseSearchForm.getContext());
                if(!CommonUtil.isEmpty(value)){
                    //如果这个热词不为空
                    Integer hotWordCount = Integer.valueOf(value);
                    hotWordCount = hotWordCount + 1;
                    opsForHash.put(HOT_WORD_REDIS_KEY_TEMPLATE,
                            houseSearchForm.getContext(),
                            String.valueOf(hotWordCount));
                    if(hotWordCount == 3){
                        logger.info("正在准备写入热词...");
                        //查询次数达到3次，就写入热词文本
                        File file = new File(hotWordPath);
                        //这里构造方法多了一个参数true,表示在文件末尾追加写入
                        FileOutputStream fos =new FileOutputStream(file,true);
                        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");//指定以UTF-8格式写入文件
                        //换行写入
                        osw.write("\r\n"+houseSearchForm.getContext());
                        osw.close();
                        logger.info("写入热词完毕...");
                    }
                }else{
                    //如果这个热词为空 就存入redis 访问次数记为1
                    opsForHash.put(HOT_WORD_REDIS_KEY_TEMPLATE,
                            houseSearchForm.getContext(),
                            "1");
                }
            }
            //将高亮设置添加到searchSourceBuilder中
            searchSourceBuilder.highlighter(highlightBuilder);
            //把boolQueryBuilder拼接到searchSourceBuilder中
            searchSourceBuilder.query(boolQueryBuilder);
        }
        searchRequest.indices("online_house_achieve").types("house").source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //获取查询出来的数据hits
        SearchHit[] hits = search.getHits().getHits();
        List<House> houseList = new ArrayList<>();
        for (SearchHit hit : hits) {
            //对hits数据进行转换
            House house = JSON.parseObject(hit.getSourceAsString(), House.class);
            if(houseSearchForm !=null && !CommonUtil.isEmpty(houseSearchForm.getContext())){
                //如果用户输入的搜索内容不为空  高亮处理
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                for(Map.Entry<String, HighlightField> map : highlightFields.entrySet()){
                    if("info".equals(map.getKey())){
                        house.setInfo(String.valueOf(map.getValue().fragments()[0]));
                    }else if("location".equals(map.getKey())){
                        house.setLocation(String.valueOf(map.getValue().fragments()[0]));
                    }
                }
            }
            houseList.add(house);
        }
        return ResponseVo.success(houseList);
    }

    @Override
    public ResponseVo<House> getInfoData(House house) {
        if(house == null || house.getId() == null){
            return ResponseVo.errorByMsg(CodeMsg.DATA_ERROR);
        }
        House selectedHouse = houseDao.selectById(house.getId());
        if(selectedHouse == null || selectedHouse.getUserId() == null){
            return ResponseVo.errorByMsg(CodeMsg.HOUSE_NOT_EXIST);
        }
        //获取该房屋对应的中介信息
        User user = userDao.selectById(selectedHouse.getUserId());
        if(user == null){
            return ResponseVo.errorByMsg(CodeMsg.HOUSE_NOT_EXIST);
        }
        selectedHouse.setUser(user);
        return ResponseVo.success(selectedHouse);
    }

    @Override
    public ResponseVo<Boolean> chatWithAgent(House house, HttpServletRequest request) {
        if(house == null || house.getUserId() == null){
            return ResponseVo.errorByMsg(CodeMsg.DATA_ERROR);
        }
        //判断是否登录了
        User userJWT = chatService.getUser(request).getData();
        if(userJWT == null){
            return ResponseVo.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        User user = userDao.selectById(userJWT.getId());
        User agent = userDao.selectById(house.getUserId());
        //判断中介是否在线
        if(!ChatEndpoint.onlineUsers.containsKey(house.getUserId())){
            //如果中介不在线，则给出提示
            return ResponseVo.errorByMsg(CodeMsg.AGENT_NOT_ONLINE);
        }
        //不允许自己和自己聊天
        if(user.getId().equals(house.getUserId())){
            return ResponseVo.errorByMsg(CodeMsg.CHAT_WITH_MYSELF);
        }
        //不允许中介和中介聊天
        if(UserRoleEnum.HOUSE_AGENT.getCode().equals(user.getRoleId()) && UserRoleEnum.HOUSE_AGENT.getCode().equals(agent.getRoleId())){
            return ResponseVo.errorByMsg(CodeMsg.AGENT_CHAT_WITH_AGENT);
        }
        //双方聊天用户存储到redis
        HashOperations<String, String, String> opsForHashByUserChat = stringRedisTemplate.opsForHash();
        opsForHashByUserChat.delete(USER_CHAT_REDIS_KEY_TEMPLATE, String.valueOf(user.getId()));
        opsForHashByUserChat.put(USER_CHAT_REDIS_KEY_TEMPLATE, String.valueOf(user.getId()), gson.toJson(agent));


        HashOperations<String, String, String> opsForHashByAgentChat = stringRedisTemplate.opsForHash();
        String value = opsForHashByAgentChat.get(AGENT_CHAT_REDIS_KEY_TEMPLATE, String.valueOf(house.getUserId()));
        if(CommonUtil.isEmpty(value)){
            //值为空，直接塞入
            List<User> userList = new ArrayList<>();
            userList.add(user);
            opsForHashByAgentChat.put(AGENT_CHAT_REDIS_KEY_TEMPLATE, String.valueOf(house.getUserId()), gson.toJson(userList));
        }else{
            //值不为空，先取出添加后再塞入
            List<User> userList = gson.fromJson(value, new TypeToken<List<User>>() {}.getType());
            userList.add(user);
            opsForHashByAgentChat.put(AGENT_CHAT_REDIS_KEY_TEMPLATE, String.valueOf(house.getUserId()), gson.toJson(userList));
        }

        //说明中介在线，即将跳转到聊天页面
        return ResponseVo.success(true);
    }

    @Override
    public ResponseVo<List<House>> getDataByCreateTime() throws IOException {
        List<House> houseList = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //根据上线时间降序排序，并获取前三个
        sourceBuilder.from(0).size(3).sort("createTime", SortOrder.DESC);
        searchRequest.indices("online_house_achieve").types("house").source(sourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        for (SearchHit hit : hits) {
            House house = JSON.parseObject(hit.getSourceAsString(), House.class);
            houseList.add(house);
        }
        return ResponseVo.success(houseList);
    }

    @Override
    public ResponseVo<List<House>> getRentingHouseList() throws IOException {
        List<House> houseList = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //根据上线时间降序排序，并获取前四个
        sourceBuilder.from(0).size(4).query(QueryBuilders.termQuery("category", HouseCategoryEnum.HOUSE_RENTING.getCode()));
        searchRequest.indices("online_house_achieve").types("house").source(sourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        for (SearchHit hit : hits) {
            House house = JSON.parseObject(hit.getSourceAsString(), House.class);
            houseList.add(house);
        }
        return ResponseVo.success(houseList);
    }

    @Override
    public ResponseVo<List<House>> getPurchaseHouseList() throws IOException {
        List<House> houseList = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //根据上线时间降序排序，并获取前四个
        sourceBuilder.from(0).size(4).query(QueryBuilders.termQuery("category", HouseCategoryEnum.HOUSE_PURCHASE.getCode()));
        searchRequest.indices("online_house_achieve").types("house").source(sourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        for (SearchHit hit : hits) {
            House house = JSON.parseObject(hit.getSourceAsString(), House.class);
            houseList.add(house);
        }
        return ResponseVo.success(houseList);
    }

}
