public class ComplexNumber {
    private final double re;
    private final double im;

    // Конструктор без параметров
    public ComplexNumber() {
        this(0, 0);
    }

    // Основной конструктор
    public ComplexNumber(double re, double im) {
        this.re = re;
        this.im = im;
    }

    // Get-методы
    public double getRe() { return re; }
    public double getIm() { return im; }

    // Сложение
    public ComplexNumber plus(ComplexNumber other) {
        return new ComplexNumber(this.re + other.re, this.im + other.im);
    }

    // Вычитание
    public ComplexNumber minus(ComplexNumber other) {
        return new ComplexNumber(this.re - other.re, this.im - other.im);
    }

    // Умножение: (a+bi)(c+di) = (ac - bd) + (ad + bc)i
    public ComplexNumber multiplication(ComplexNumber other) {
        double newRe = this.re * other.re - this.im * other.im;
        double newIm = this.re * other.im + this.im * other.re;
        return new ComplexNumber(newRe, newIm);
    }

    // Деление: (a+bi)/(c+di) = ((ac+bd) + (bc-ad)i) / (c^2+d^2)
    public ComplexNumber division(ComplexNumber other) {
        double denominator = other.re * other.re + other.im * other.im;
        if (denominator == 0.0) {
            throw new ArithmeticException("Деление на ноль");
        }
        double newRe = (this.re * other.re + this.im * other.im) / denominator;
        double newIm = (this.im * other.re - this.re * other.im) / denominator;
        return new ComplexNumber(newRe, newIm);
    }

    // Модуль комплексного числа
    public double modulus() {
        return Math.sqrt(re * re + im * im);
    }

    // Проверка на равенство комплексного числа нулю
    public boolean isZero() {
        return re == 0.0 && im == 0.0;
    }

    // Перевод комплексного числа в строку
    public String toString() {
        if (im == 0) return String.valueOf(re);
        if (re == 0) return String.valueOf(im) + "i";
        String sign = " ";
        if (im < 0) {
            sign = " - ";
        }
        else {
            sign = " + ";
        }
        return String.valueOf(re) + sign + String.valueOf(Math.abs(im)) + "i";
    }

}