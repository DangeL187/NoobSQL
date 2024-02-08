#include <vector>

std::vector<int> maxLengthsInColumns(std::vector<std::vector<std::string>>& vec) {
    std::vector<int> max_lengths;
    if (vec.size() > 0) {
        for (int i = 0; i < vec[0].size(); i++) { //columns
            int max = -1;
            for (int j = 0; j < vec.size(); j++) { //rows
                int len = vec[j][i].length();
                if (max == -1 || len > max) {
                    max = len;
                }
            }
            max_lengths.push_back(max);
        }
    }
    return max_lengths;
}