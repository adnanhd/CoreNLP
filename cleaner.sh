cd raw
for file in *; do
    tr -cd '\11\12\15\40-\176' < $file | nroff > ../data/$file
done
cd ..
