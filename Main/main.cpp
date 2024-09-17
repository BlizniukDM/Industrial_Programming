#include <iostream>
#include <fstream>
#include <string>
#include <stdexcept>
#include <sstream>
#include <stack>
#include <cctype>

using namespace std;

// Обработка данных
class IDataProcessor 
{
public:
    virtual string process(const string& data) = 0;
    virtual ~IDataProcessor() {}
};

// Арифметические операции с учетом приоритета
class ArithmeticProcessor : public IDataProcessor 
{
private:
    int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return 0;
    }

    int applyOperation(int a, int b, int op) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0) throw runtime_error("Division by zero");
                return a / b;
        }
        return 0;
    }

public:
    string process(const string& data) 
    {
        istringstream stream(data);
        stack<int> values; // числа
        stack<char> ops;      // операторы
        int number;

        while (stream >> number) 
        {
            values.push(number);
            char operation;
            if (stream >> operation) 
            {
                while (!ops.empty() && precedence(ops.top()) >= precedence(operation)) 
                {
                    int val2 = values.top(); values.pop();
                    int val1 = values.top(); values.pop();
                    char op = ops.top(); ops.pop();
                    values.push(applyOperation(val1, val2, op));
                }
                ops.push(operation);
            }
        }

        // Обработка оставшихся операторов
        while (!ops.empty()) 
        {
            int val2 = values.top(); values.pop();
            int val1 = values.top(); values.pop();
            char op = ops.top(); ops.pop();
            values.push(applyOperation(val1, val2, op));
        }

        return to_string(values.top());
    }
};

// Работа с файлами
class FileHandler 
{
public:
    static string readFile(const string& filePath) 
    {
        ifstream file(filePath);
        if (!file.is_open()) 
        {
            throw runtime_error("Cannot open file: " + filePath);
        }
        string content((istreambuf_iterator<char>(file)), istreambuf_iterator<char>());
        return content;
    }

    static void writeFile(const string& filePath, const string& data) 
    {
        ofstream file(filePath);
        if (!file.is_open())
        {
            throw runtime_error("Cannot open file: " + filePath);
        }
        file << data;
    }
};

// Основной класс
class Application {
private:
    string inputFile;
    string outputFile;
    IDataProcessor* processor;

public:
    Application(const string& input, const string& output)
        : inputFile(input), outputFile(output), processor(new ArithmeticProcessor()) {}

    ~Application() { delete processor; }

    void run() 
    {
        try 
        {
            string data = FileHandler::readFile(inputFile);
            string processedData = processor->process(data);
            FileHandler::writeFile(outputFile, processedData);

            cout << "Processing completed successfully." << endl;
        } 
        catch (const exception& e) 
        {
            cerr << "Error: " << e.what() << endl;
        }
    }
};

int main(int argc, char* argv[]) 
{
    if (argc != 3) 
    {
        cerr << "Usage: " << argv[0] << " input_file output_file" << endl;
        return 1;
    }

    Application app(argv[1], argv[2]);
    app.run();

    return 0;
}