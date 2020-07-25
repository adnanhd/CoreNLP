cd data
for file in *; do
    pwd
    tr -cd '\11\12\15\40-\176' < $file > ../test/$file
done
cd ..