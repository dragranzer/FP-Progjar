public class Player {
    private Integer point;

    public void plus(Integer n){
        setPoint(getPoint() + n);
    }

    public void minus(Integer n){
        setPoint(getPoint() - n);
    }

    public void times(Integer n){
        setPoint(getPoint() * n);
    }

    public void divide(Integer n){
        setPoint(getPoint() / n);
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
}
