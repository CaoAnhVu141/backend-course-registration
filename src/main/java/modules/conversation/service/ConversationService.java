package modules.conversation.service;

import lombok.RequiredArgsConstructor;
import modules.auth.entity.User;
import modules.auth.repository.UserRepository;
import modules.conversation.dto.ChatMessageResponse;
import modules.conversation.dto.ConversationResponse;
import modules.conversation.entity.ChatMessage;
import modules.conversation.entity.Conversation;
import modules.conversation.entity.ConversationMember;
import modules.conversation.enums.EnumChatError;
import modules.conversation.exception.ChatException;
import modules.conversation.repository.ChatMessageRepository;
import modules.conversation.repository.ConversationMemberRepository;
import modules.conversation.repository.ConversationRepository;
import modules.student_group.repository.GroupMemberRepository;
import modules.topic_registrations.entity.TopicRegistration;
import modules.topic_registrations.repository.TopicRegistrationRepository;
import modules.topics.repository.TopicSupervisorRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service

@RequiredArgsConstructor
public class ConversationService {

    // tự động tạo cuộc trò chuyện khi được duyệt chấp nhận
    @Transactional
    public ConversationResponse createTopicConversation(
            Long topicRegistrationId) {
        if (conversationRepository.existsByTopicRegistrationId(topicRegistrationId)) {
            return getConversationByTopicRegistration(topicRegistrationId);
        }

        TopicRegistration topicRegistration = topicRegistrationRepository.findById(topicRegistrationId)
                .orElseThrow(() -> new ChatException(
                        EnumChatError.CONVERSATION_NOT_FOUND));

        Conversation conversation = conversationRepository.save(
                Conversation.builder()
                        .topicRegistration(topicRegistration)
                        .name("Trao đổi đề tài - "
                                + topicRegistration.getTopic().getTitle())
                        .build());

        Set<Long> memberUserIds = new HashSet<>();

        groupMemberRepository.findAllByStudentGroupId(
                topicRegistration.getStudentGroup().getId())
                .forEach(groupMember -> memberUserIds.add(
                        groupMember.getStudent().getUserId()));

        topicSupervisorRepository.findAllByTopicId(
                topicRegistration.getTopic().getId())
                .forEach(topicSupervisor -> memberUserIds.add(
                        topicSupervisor.getLecturer().getUserId()));

        List<ConversationMember> members = new ArrayList<>();

        for (Long userId : memberUserIds) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ChatException(
                            EnumChatError.USER_NOT_FOUND));

            members.add(
                    ConversationMember.builder()
                            .conversation(conversation)
                            .user(user)
                            .build());
        }

        conversationMemberRepository.saveAll(members);

        return toConversationResponse(conversation, memberUserIds);
    }

    @Transactional(readOnly = true)
    public ConversationResponse getConversationByTopicRegistration(
            Long topicRegistrationId) {
        Conversation conversation = conversationRepository
                .findByTopicRegistrationId(topicRegistrationId)
                .orElseThrow(() -> new ChatException(
                        EnumChatError.CONVERSATION_NOT_FOUND));

        List<Long> memberIds = conversationMemberRepository
                .findAllByConversationId(conversation.getId())
                .stream()
                .map(member -> member.getUser().getId())
                .toList();

        return toConversationResponse(conversation, memberIds);
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getMessages(
            Long conversationId,
            String username) {
        User user = findUser(username);
        checkMember(conversationId, user.getId());

        return chatMessageRepository
                .findAllByConversationIdOrderByCreatedAtAsc(conversationId)
                .stream()
                .map(this::toMessageResponse)
                .toList();
    }

    @Transactional
    public ChatMessageResponse sendMessage(
            Long conversationId,
            String content,
            String username) {
        User user = findUser(username);
        checkMember(conversationId, user.getId());

        if (content == null || content.trim().isEmpty()) {
            throw new ChatException(EnumChatError.MESSAGE_CONTENT_EMPTY);
        }

        Conversation conversation = conversationRepository.findById(
                conversationId)
                .orElseThrow(() -> new ChatException(
                        EnumChatError.CONVERSATION_NOT_FOUND));

        ChatMessage message = chatMessageRepository.save(
                ChatMessage.builder()
                        .conversation(conversation)
                        .sender(user)
                        .content(content.trim())
                        .build());

        ChatMessageResponse response = toMessageResponse(message);

        messagingTemplate.convertAndSend(
                "/topic/conversations/" + conversationId,
                response);

        return response;
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ChatException(
                        EnumChatError.USER_NOT_FOUND));
    }

    private void checkMember(Long conversationId, Long userId) {
        if (!conversationMemberRepository
                .existsByConversationIdAndUserId(conversationId, userId)) {
            throw new ChatException(
                    EnumChatError.NOT_CONVERSATION_MEMBER);
        }
    }

    private ConversationResponse toConversationResponse(
            Conversation conversation,
            List<Long> memberIds) {
        return ConversationResponse.builder()
                .id(conversation.getId())
                .topicRegistrationId(
                        conversation.getTopicRegistration().getId())
                .name(conversation.getName())
                .memberUserIds(memberIds)
                .build();
    }

    private ConversationResponse toConversationResponse(
            Conversation conversation,
            Set<Long> memberIds) {
        return toConversationResponse(
                conversation,
                memberIds.stream().toList());
    }

    private ChatMessageResponse toMessageResponse(
            ChatMessage message) {
        return ChatMessageResponse.builder()
                .id(message.getId())
                .conversationId(message.getConversation().getId())
                .senderId(message.getSender().getId())
                .senderName(message.getSender().getName())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
