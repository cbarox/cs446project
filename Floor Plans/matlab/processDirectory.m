function processDirectory(dirName)
    fileList = getImgFiles(dirName);
    if(~isempty(fileList))
        for i = 1:size(fileList,1)
            tic;
            x = char(fileList(i));
            [bboxes, rooms, ~] = process(imread(x));
            filename = strrep(x, '.png', '.json');
            fileID = fopen(filename, 'w');
            fwrite(fileID, toJson(bboxes, rooms));
            fclose(fileID);
            toc;
        end
    end
end