public class ComplexMatrix {
    private final ComplexNumber[][] data;
    private final int rows;
    private final int cols;

    private static final ComplexNumber ZERO = new ComplexNumber(0, 0);

    // Конструктор без параметров
    public ComplexMatrix() {
        this(1,1);
    }

    // Конструктор матрицы из нулей
    public ComplexMatrix(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Размеры матрицы должны быть положительными");
        }
        this.rows = rows;
        this.cols = cols;
        this.data = new ComplexNumber[rows][cols];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                data[i][j] = ZERO;
            }
        }
    }

    // Конструктор матрицы из значений, переданных пользователем
    public ComplexMatrix(ComplexNumber[][] data) {
        if (data == null || data.length == 0 || data[0].length == 0) {
            throw new IllegalArgumentException("Некорректные данные матрицы");
        }
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = new ComplexNumber[rows][cols];
        for (int i = 0; i < rows; ++i) {
            if (data[i].length != cols) {
                throw new IllegalArgumentException("Строки матрицы разной длины");
            }
            for (int j = 0; j < cols; ++j) {
                if (data[i][j] != null) {
                    this.data[i][j] = data[i][j];
                }
                else {
                    this.data[i][j] = ZERO;
                }
            }
        }
    }

    // Get-методы
    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public ComplexNumber get(int i, int j) { return data[i][j]; }

    // Set-метод
    public void set(int i, int j, ComplexNumber value) { data[i][j] = value; }

    // Проверка факта, является ли матрица квадратной
    public boolean isSquare() { return rows == cols; }

    // Проверка на равенство размеров матриц
    private void requireSameSize(ComplexMatrix other, String operation) {
        if (this.rows != other.rows || this.cols != other.cols) {
            throw new IllegalArgumentException(
                    "Для " + operation + " матрицы должны быть одного размера");
        }
    }

    // Сложение
    public ComplexMatrix add(ComplexMatrix other) {
        requireSameSize(other, "сложения");
        ComplexMatrix result = new ComplexMatrix(rows, cols);
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                result.data[i][j] = this.data[i][j].plus(other.data[i][j]);
            }
        }
        return result;
    }

    // Вычитание
    public ComplexMatrix subtract(ComplexMatrix other) {
        requireSameSize(other, "вычитания");
        ComplexMatrix result = new ComplexMatrix(rows, cols);
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                result.data[i][j] = this.data[i][j].minus(other.data[i][j]);
            }
        }
        return result;
    }

    // Умножение
    public ComplexMatrix multiply(ComplexMatrix other) {
        if (this.cols != other.rows) {
            throw new IllegalArgumentException(
                    "Несовместимые размеры для умножения: " + this.rows + "x" + this.cols + " и " + other.rows + "x" + other.cols);
        }
        ComplexMatrix result = new ComplexMatrix(this.rows, other.cols);
        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < other.cols; ++j) {
                ComplexNumber sum = ZERO;
                for (int k = 0; k < this.cols; ++k) {
                    sum = sum.plus(this.data[i][k].multiplication(other.data[k][j]));
                }
                result.data[i][j] = sum;
            }
        }
        return result;
    }

    // Деление (A / B = A * B^(-1))
    public ComplexMatrix divide(ComplexMatrix other) {
        if (!other.isSquare()) {
            throw new IllegalArgumentException("Делитель должен быть квадратной матрицей");
        }
        if (this.cols != other.rows) {
            throw new IllegalArgumentException(
                    "Несовместимые размеры для деления: " + this.rows + "x" + this.cols + " и " + other.rows + "x" + other.cols);
        }
        ComplexMatrix inverse = other.inverse();
        return this.multiply(inverse);
    }

    // Транспонирование
    public ComplexMatrix transpose() {
        ComplexMatrix result = new ComplexMatrix(cols, rows);
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                result.data[j][i] = this.data[i][j];
            }
        }
        return result;
    }

    // Определитель
    public ComplexNumber determinant() {
        if (!isSquare()) {
            throw new IllegalArgumentException("Определитель определён только для квадратных матриц");
        }
        return determinantRecursive(this.data, rows);
    }

    // Вычисление определителя через рекурсию
    private static ComplexNumber determinantRecursive(ComplexNumber[][] m, int n) {
        if (n == 1) return m[0][0];
        if (n == 2) {
            return m[0][0].multiplication(m[1][1]).minus(m[0][1].multiplication(m[1][0]));
        }
        ComplexNumber det = ZERO;
        for (int col = 0; col < n; ++col) {
            ComplexNumber[][] minor = buildMinor(m, n, 0, col);
            ComplexNumber term = m[0][col].multiplication(determinantRecursive(minor, n - 1));
            // Определить знак: (-1)^col
            if (col % 2 == 0) {
                det = det.plus(term);
            } else {
                det = det.minus(term);
            }
        }
        return det;
    }

    // Найти минор
    private static ComplexNumber[][] buildMinor(ComplexNumber[][] m, int n, int skipRow, int skipCol) {
        ComplexNumber[][] minor = new ComplexNumber[n - 1][n - 1];
        int mi = 0;
        for (int i = 0; i < n; ++i) {
            if (i == skipRow) continue;
            int mj = 0;
            for (int j = 0; j < n; ++j) {
                if (j == skipCol) continue;
                minor[mi][mj] = m[i][j];
                ++mj;
            }
            ++mi;
        }
        return minor;
    }

    // Обратная матрица
    public ComplexMatrix inverse() {
        if (!isSquare()) {
            throw new IllegalArgumentException("Обратная матрица существует только для квадратных матриц");
        }
        ComplexNumber det = determinant();
        if (det.isZero()) {
            throw new ArithmeticException("Матрица вырождена, обратной не существует");
        }

        int n = rows;
        ComplexMatrix result = new ComplexMatrix(n, n);

        if (n == 1) {
            result.data[0][0] = new ComplexNumber(1, 0).division(det);
            return result;
        }

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                ComplexNumber[][] minor = buildMinor(data, n, i, j);
                ComplexNumber algebraic_addition = determinantRecursive(minor, n - 1);
                // Алгебраическое дополнение A_ij = (-1)^(i+j) * M_ij
                if ((i + j) % 2 != 0) {
                    algebraic_addition = new ComplexNumber(-algebraic_addition.getRe(), -algebraic_addition.getIm());
                }
                // Присоединённая матрица
                result.data[j][i] = algebraic_addition.division(det);
            }
        }
        return result;
    }

    // Перевод в строку
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; ++i) {
            sb.append("[ ");
            for (int j = 0; j < cols; ++j) {
                sb.append(data[i][j]);
                if (j < cols - 1) sb.append(" , ");
            }
            sb.append(" ]");
            if (i < rows - 1) sb.append("\n");
        }
        return sb.toString();
    }
}