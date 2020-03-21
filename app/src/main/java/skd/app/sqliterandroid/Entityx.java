package skd.app.sqliterandroid;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Entityx {
    @Id(autoincrement = true)
    public Long id;

    String column2="hii";
    String column1="bye";
    String column3="gye";
    @Generated(hash = 1360898827)
    public Entityx(Long id, String column2, String column1, String column3) {
        this.id = id;
        this.column2 = column2;
        this.column1 = column1;
        this.column3 = column3;
    }
    @Generated(hash = 1935969146)
    public Entityx() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getColumn2() {
        return this.column2;
    }
    public void setColumn2(String column2) {
        this.column2 = column2;
    }
    public String getColumn1() {
        return this.column1;
    }
    public void setColumn1(String column1) {
        this.column1 = column1;
    }
    public String getColumn3() {
        return this.column3;
    }
    public void setColumn3(String column3) {
        this.column3 = column3;
    }


}
