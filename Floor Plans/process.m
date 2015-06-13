function process(dirName)
    fileList = getImgFiles(dirName);
    if(~isempty(fileList))
        result = cellfun(@(x) scanimg(imread(x)), fileList, 'UniformOutput', false);
        files = cellfun(@(x) strrep(x, '.png', '.json'), fileList, 'UniformOutput', false);
        for i = 1:size(files,1)
            fileID = fopen(char(files(i)), 'w');
            fwrite(fileID, char(result(i)));
            fclose(fileID);
        end
    end
end