package kamkeel.npcs.network.enums;
public enum EnumRequestPacket {
    NPCDelete,
    RemoteMainMenu,
    RemoteGlobalMenu,
    NpcClose,
    RemoteDelete,
    RemoteFreeze,
    RemoteFreezeGet,
    RemoteReset,
    SpawnMob,
    MobSpawner,

    DimensionsGet,

    MainmenuAISave,
    MainmenuAIGet,
    MainmenuInvSave,
    MainmenuInvGet,
    MainmenuStatsSave,
    MainmenuStatsGet,
    MainmenuDisplaySave,
    MainmenuDisplayGet,
    ModelDataSave,
    MainmenuAdvancedSave,
    MainmenuAdvancedGet,
    MainmenuAdvancedMarkData,

    DialogNpcSet,
    DialogNpcRemove,
    DialogNpcGet,
    DialogCategorySave,
    DialogCategoriesGet,
    DialogsGetFromDialog,
    DialogCategoryRemove,
    DialogCategoryGet,
    DialogSave,
    DialogsGet,
    DialogGet,
    DialogRemove,

    TransportSave,
    TransportCategoriesGet,
    TransportCategorySave,
    TransportCategoryRemove,
    TransportRemove,
    TransportGetLocation,
    TransportsGet,

    FactionSet,
    FactionSave,
    FactionsGet,
    FactionGet,
    FactionRemove,

    TagSet,
    TagSave,
    TagsGet,
    TagGet,
    NpcTagsGet,
    TagRemove,

    QuestUntrack,
    QuestLogToServer,
    QuestCategorySave,
    QuestCategoriesGet,
    QuestRemove,
    QuestCategoryRemove,
    QuestRewardSave,
    QuestSave,
    QuestsGetFromQuest,
    QuestsGet,
    QuestDialogGetTitle,
    QuestOpenGui,
    QuestGet,

    RecipeSave,
    RecipeRemove,
    RecipesGet,
    RecipeGet,

    NaturalSpawnSave,
    NaturalSpawnGet,
    NaturalSpawnRemove,
    NaturalSpawnGetAll,

    MerchantUpdate,
    PlayerRider,
    SpawnRider,
    MovingPathSave,
    MovingPathGet,

    AnimationsGet,
    AnimationGet,
    AnimationRemove,
    AnimationSave,

    RemoteNpcsGet,
    RemoteTpToNpc,
    SaveTileEntity,
    TileEntityGet,
    Gui,
    IsGuiOpen,
    CacheAnimation,

    CustomGuiButton,
    CustomGuiScrollClick,
    CustomGuiClose,
    CustomGuiUnfocused,

    PartyLogToServer,
    PartySave,
    PartyInfo,
    PartyDisband,
    PartyKick,
    PartyLeave,
    PartyInvite,
    PartyInviteList,
    PartyAcceptInvite,
    PartyIgnoreInvite,
    PartySetLeader,
    PartySetQuest,

    LinkedRemove,
    LinkedAdd,
    LinkedGetAll,
    LinkedSet,

    BankGet,
    BanksGet,
    BankSave,
    BankRemove,

    QuestCategoryGet,

    PlayerDataGet,
    PlayerDataRemove,
    PlayerDataMapRegen,

    JobSave,
    JobGet,
    JobSpawnerAdd,
    JobSpawnerRemove,

    RoleCompanionUpdate,
    RoleSave,
    RoleGet,

    MailOpenSetup,

    TransformSave,
    TransformGet,
    TransformLoad,

    TraderMarketSave,

    ServerUpdateSkinOverlays
}
