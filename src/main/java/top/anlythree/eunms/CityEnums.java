package top.anlythree.eunms;


public enum CityEnums {

    HANG_ZHOU(12,"杭州","hangzhou");

    private Integer id;

    private String name;

    private String enName;





    CityEnums(Integer id, String name, String enName) {
        this.id = id;
        this.name = name;
        this.enName = enName;
    }
}
