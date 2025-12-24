package com.hirewatch.core.service;

import com.hirewatch.core.dto.InquiryRequest;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.model.block.composition.TextObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;


@Service
public class InquiryService {

    // yml정보 가져오기
    @Value(value = "${slack.token}")
    private String token;
    @Value(value = "${slack.channel.monitor}")
    private String channel;

    public void postInquiry(InquiryRequest inquiryRequestDto) throws IOException {

        // Slack 메세지 보내기
        try{
            List<TextObject> textObjects = new ArrayList<>();
            textObjects.add(markdownText("*이름:*\n" + inquiryRequestDto.getName()));
           // textObjects.add(markdownText("*문의 남긴 날짜:*\n" + inquiry.getCreatedAt()));
            textObjects.add(markdownText("*문의 제목:*\n" + inquiryRequestDto.getTitle()));
            textObjects.add(markdownText("*문의내용:*\n" + inquiryRequestDto.getCont()));

            MethodsClient methods = Slack.getInstance().methods(token);
            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(channel)
                    .blocks(asBlocks(
                            header(header -> header.text(plainText( inquiryRequestDto.getName() + "님이 문의를 남겨주셨습니다!"))),
                            divider(),
                            section(section -> section.fields(textObjects)
                            ))).build();

            methods.chatPostMessage(request);
        } catch (SlackApiException | IOException e) {
           //  new ApiException(ErrorCode.INTERNAL_SERVER_ERROR, "Can't send Slack Message.");
            System.out.println("123134");
        }
    }
}
