// Импортируем класс Scanner для чтения вводимых пользователем данных
import java.util.Scanner;

public class Main {
    // Объект типа Scanner для чтения ввода с клавиатуры
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Работа с матрицами комплексных чисел ===");

        while (true) {
            try {
                ComplexMatrix a = readMatrix("Матрица A");
                ComplexMatrix b = null;

                System.out.println("\nМатрица A:");
                System.out.println(a);

                System.out.println("\nВыберите операцию:");
                System.out.println("1) Сложение (A + B)");
                System.out.println("2) Умножение (A * B)");
                System.out.println("3) Деление   (A / B)");
                System.out.println("4) Транспонирование (A^T)");
                System.out.println("5) Определитель (det A)");
                System.out.println("0) Выход");
                System.out.print("Ваш выбор: ");

                // Нужно для чтения введённой с клавиатуры строки
                // Integer.parseInt() превращает строку в int
                // scanner.nextLine() читает всё, что ввёл пользователь до нажатия клавиши Enter
                // trim() убирает лишние пробелы
                int choice = Integer.parseInt(scanner.nextLine().trim());

                if (choice == 0) {
                    System.out.println("До свидания!");
                    return;
                }

                if (choice >= 1 && choice <= 3) {
                    b = readMatrix("Матрица B");
                    System.out.println("\nМатрица B:");
                    System.out.println(b);
                }

                System.out.println("\n----- Результат -----");
                switch (choice) {
                    case 1 -> System.out.println(a.add(b));
                    case 2 -> System.out.println(a.multiply(b));
                    case 3 -> System.out.println(a.divide(b));
                    case 4 -> System.out.println(a.transpose());
                    case 5 -> System.out.println("det(A) = " + a.determinant());
                    default -> System.out.println("Неизвестная операция");
                }
                System.out.println("---------------------\n");

            }
            catch (NumberFormatException e) {
                System.out.println("Ошибка: введите корректное число.\n");
            }
            // getMessage() нужен для получения сообщений об ошибках,
            // прописанных в данном случае в классах ComplexNumber и ComplexMatrix
            catch (IllegalArgumentException | ArithmeticException e) {
                System.out.println("Ошибка операции: " + e.getMessage() + "\n");
            }
        }
    }

    // Вводимая пользователем матрица
    private static ComplexMatrix readMatrix(String name) {
        int rows = readPositiveInt("Введите количество строк для " + name + ": ");
        int cols = readPositiveInt("Введите количество столбцов для " + name + ": ");

        ComplexMatrix matrix = new ComplexMatrix(rows, cols);
        System.out.println("Введите элементы " + name + " в формате 're im' (например, '3 -2' = 3-2i):");

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                while (true) {
                    System.out.print("  [" + (i + 1) + "][" + (j + 1) + "]: ");
                    String line = scanner.nextLine().trim().replace(',', '.');
                    // line.split("\\s+") позволяет разделить вводимую пользователем строку на части, разделённые пробелами
                    String[] parts = line.split("\\s+");
                    if (parts.length == 2) {
                        try {
                            double re = Double.parseDouble(parts[0]);
                            double im = Double.parseDouble(parts[1]);
                            matrix.set(i, j, new ComplexNumber(re, im));
                            break;
                        }
                        catch (NumberFormatException e) { }
                    }
                    System.out.println("Неверный формат. Введите два числа: re im");
                }
            }
        }
        return matrix;
    }

    // Нужен для того, чтобы заставить пользователя ввести положительное число
    // (например, при введении чисел строк и столбцов матрицы)
    private static int readPositiveInt(String entered) {
        while (true) {
            System.out.print(entered);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value > 0) return value;
                System.out.println("Число должно быть больше нуля.");
            }
            catch (NumberFormatException e) {
                System.out.println("Введите целое число.");
            }
        }
    }
}