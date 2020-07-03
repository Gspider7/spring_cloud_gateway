import org.junit.Test;

/**
 * @author xutao
 * @date 2019-01-19 16:41
 */
public class BaseTest {

    @Test
    public void testJson2() {
        String s = "{\"id\":9,\"projectId\":5,\"name\":\"test\",\"description\":\"test\",\"displayInfo\":\"{\\\"components\\\":[{\\\"id\\\":1190594577,\\\"itemsType\\\":\\\"pattern-analyzer\\\",\\\"name\\\":\\\"模糊匹配组件\\\",\\\"projectId\\\":5,\\\"inputIds\\\":[1243106609],\\\"outputIds\\\":[],\\\"svg\\\":\\\"fuzzy_match\\\",\\\"type\\\":\\\"FUZZY_MATCHER\\\",\\\"x\\\":612.0802612304687,\\\"y\\\":-20.5989351272583},{\\\"id\\\":1243106609,\\\"itemsType\\\":\\\"writer\\\",\\\"name\\\":\\\"读取器\\\",\\\"projectId\\\":5,\\\"inputIds\\\":[],\\\"outputIds\\\":[1190594577],\\\"svg\\\":\\\"reader\\\",\\\"type\\\":\\\"DATA_READER\\\",\\\"x\\\":361.6363525390625,\\\"y\\\":-82.81816828250885}],\\\"transform\\\":{\\\"k\\\":1,\\\"x\\\":191.3636474609375,\\\"y\\\":219}}\",\"componentList\":[{\"fuzzyMatcherVO\":{\"matchType\":\"target\",\"quoteDataIdList\":[2,10],\"filterDataPercentLow\":30,\"filterDataPercentHigh\":100,\"columnPairList\":[{\"quoteDataIdList\":true,\"algorithm\":\"String\",\"sourceColumnName\":\"ID\",\"weight\":80,\"targetColumnName\":\"ID\"},{\"quoteDataIdList\":[10],\"algorithm\":\"String\",\"sourceColumnName\":\"NAME\",\"weight\":20,\"targetColumnName\":\"NAME\"}],\"type\":\"1\",\"targetSourceId\":17,\"targetSourceName\":\"test\",\"targetCacheId\":null,\"targetCacheName\":null,\"targetTableName\":\"DOMAIN\"},\"name\":\"模糊匹配组件\",\"description\":null,\"type\":\"FUZZY_MATCHER\",\"componentId\":1190594577,\"preIdList\":\"[1243106609]\"},{\"componentId\":1243106609,\"type\":\"DATA_READER\",\"name\":\"读取器\",\"description\":\"ghjgj\",\"dataReaderVO\":{\"type\":\"1\",\"sample\":\"2\",\"sourceId\":17,\"sourceName\":\"test\",\"cacheId\":null,\"cacheName\":null,\"tableName\":\"DOMAIN\",\"whereConditions\":null,\"whereClause\":\"id is not null\",\"dataPercent\":null,\"dataCountStart\":40,\"dataCountEnd\":10000,\"dataLinkId\":14},\"fuzzyMatcherVO\":null,\"lengthAnalyserVO\":null,\"preIdList\":\"[]\"}],\"executionFlag\":false,\"executionStatus\":null,\"lastExecutionSuccess\":false,\"lastExecutionStartTime\":\"2019-01-24T08:00:36.000+0000\",\"lastExecutionEndTime\":\"2019-01-24T08:00:37.000+0000\"}";

        System.out.println(s);
    }
}
