package elliot.hoi.tech.Tech;

abstract class TechObjectBasic {
    abstract public Integer getId();
    abstract public String getName();
    abstract public String getDetails();

    abstract void setId(Integer id);
    abstract void setName(String name);
    abstract void setDetails(String details);
}