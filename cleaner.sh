cd data
for file in *; do
    tr -cd '\11\12\15\40-\176' < $file | nroff > ../tst/$file
done
cd ..