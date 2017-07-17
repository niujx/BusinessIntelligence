package dao;

import com.business.intelligence.model.ElemeModel.ElemeEvaluate;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Tcqq on 2017/7/17.
 */

@Slf4j
public class ElemeDao {
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public void insertEvaluate(ElemeEvaluate elemeEvaluate){
        sqlSessionTemplate.insert("com.business.intelligence.model.ElemeModel.ElemeEvaluate.insertEvaluate",elemeEvaluate);
    }
}
