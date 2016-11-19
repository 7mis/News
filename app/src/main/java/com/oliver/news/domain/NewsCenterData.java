package com.oliver.news.domain;

import java.util.List;

/**
 * @desc 新闻中心的数据 ---> GsonFormat
 * @auther Oliver
 * @time 2016/11/19 23:37
 */
public class NewsCenterData {


    /**
     * 根节点 json 目录下的三个子项
     */
    public int retcode;//Array
    public List<Data> data;//Array
    public List<String> extentd;//200

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public List<String> getExtentd() {
        return extentd;
    }

    public void setExtentd(List<String> extentd) {
        this.extentd = extentd;
    }

    /**
     * data目录下四个子项
     */
    public class Data {
        public List<Children> children;

        public class Children {
            public String id;//	10007
            public String title;//	北京
            public String type;//	1
            public String url;//	/10007/list_1.json

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public String id;//	10000
        public String title;//	新闻
        public String type;//	1
        public String url;//	/10006/list_1.json
        public String url1;//	/10007/list1_1.json

        public String dayurl;
        public String excurl;

        public String weekurl;

        public List<Children> getChildren() {
            return children;
        }

        public void setChildren(List<Children> children) {
            this.children = children;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl1() {
            return url1;
        }

        public void setUrl1(String url1) {
            this.url1 = url1;
        }

        public String getDayurl() {
            return dayurl;
        }

        public void setDayurl(String dayurl) {
            this.dayurl = dayurl;
        }

        public String getExcurl() {
            return excurl;
        }

        public void setExcurl(String excurl) {
            this.excurl = excurl;
        }

        public String getWeekurl() {
            return weekurl;
        }

        public void setWeekurl(String weekurl) {
            this.weekurl = weekurl;
        }
    }


}
